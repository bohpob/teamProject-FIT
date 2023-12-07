package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.Group;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AmountService amountService;
    private final TransactionMapper transactionMapper;

    public Transaction create(TransactionCreateRequest request, User payer, Group group) throws Exception {
        Transaction transaction = TransactionConverter.fromCreateDto(request, payer, group);

        try {
            List<Amount> amounts = amountService.setAmounts(transaction, request.getSpenders(), request.getSplitStrategy());
            transactionRepository.save(transaction);
            amountService.saveAll(amounts);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return transaction;
    }

    public Optional<Transaction> read(Long transactionId, Long groupId) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty()) {
            throw new Exception("Transaction not found.");
        }
        if (!Objects.equals(transaction.get().getGroup().getId(), groupId)) {
            throw new Exception("Transaction does not belong to this group.");
        }
        return transaction;
    }

    public TransactionReadGroupTransactionResponse readGroupTransaction(Long transactionId, Long groupId) throws Exception {
        Transaction transaction = read(transactionId, groupId)
                .orElseThrow(() -> new Exception("Transaction not found"));
        return transactionMapper.entityToReadGroupTransactionResponse(transaction);
    }

    @Transactional
    public void update(Transaction transaction, TransactionUpdateRequest request, User nextPayer) throws Exception {
        try {
            amountService.deleteAllByTransactionId(transaction.getId());
            transaction.setName(request.getName());
            transaction.setDate(request.getDate());
            transaction.setAmount(request.getAmount());
            transaction.setPayer(nextPayer);

            List<Amount> amounts = amountService.setAmounts(transaction, request.getSpenders(), request.getSplitStrategy());
            amountService.saveAll(amounts);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void delete(Transaction transaction) throws Exception {
        amountService.deleteAllByIds(transaction.getAmounts().stream().map(Amount::getId).collect(Collectors.toList()));
        transactionRepository.deleteById(transaction.getId());
    }

    public List<Transaction> readTransactions(Long groupId) {
        return transactionRepository.findTransactionsByGroupId(groupId);
    }

}
