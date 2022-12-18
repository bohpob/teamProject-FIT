package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AmountService amountService;

    public Transaction create(TransactionCreateRequest transactionCreateRequest, User payer, Group group) throws Exception {
        Transaction transaction = TransactionConverter.fromCreateDto(transactionCreateRequest, payer, group);

        try {
            List<Amount> amounts = amountService.setAmounts(transactionCreateRequest.getSpenderIds(), transaction);
            transactionRepository.save(transaction);
            amountService.saveAll(amounts);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return transaction;
    }

    public Optional<Transaction> read(Long transaction_id, Long group_id) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transaction_id);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        if (!Objects.equals(transaction.get().getGroup().getId(), group_id))
            throw new Exception("Transaction does not belong to this group.");
        return transaction;
    }

    @Transactional
    public void update(Transaction transaction, TransactionUpdateRequest transactionUpdateRequest, User nextPayer) throws Exception {
        try {
            amountService.deleteAllByTransactionId(transaction.getId());
            transaction.setName(transactionUpdateRequest.getName());
            transaction.setDate(transactionUpdateRequest.getDate());
            transaction.setAmount(transactionUpdateRequest.getAmount());
            transaction.setPayer(nextPayer);

            List<Amount> amounts = amountService.setAmounts(transactionUpdateRequest.getSpenderIds(), transaction);
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

    public ArrayList<Transaction> getTransactionsByGroupId(Long groupId) {
        return transactionRepository.getTransactionsByGroupId(groupId);
    }

}
