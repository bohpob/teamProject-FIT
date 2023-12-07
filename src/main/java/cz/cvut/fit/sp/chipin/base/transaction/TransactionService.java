package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AmountService amountService;
    private final TransactionMapper transactionMapper;

    public Transaction create(TransactionCreateRequest request, UserAccount payer, UserGroup userGroup) throws Exception {
        Transaction transaction = TransactionConverter.fromCreateDto(request, payer, userGroup);
        //TODO: replace with Request field
        transaction.setCategory(Category.NO_CATEGORY);

        try {
            List<Amount> amounts = amountService.setAmounts(request.getSpenderIds(), transaction);
            transactionRepository.save(transaction);
            amountService.saveAll(amounts);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return transaction;
    }

    //new
    public List<TransactionReadGroupTransactionsResponse> readGroupTransactions(
            Long groupId, List<Category> categories, String dateFrom, String dateTo, List<Member> members) throws Exception {
        try {
            Set<Transaction> transactionsSet = new HashSet<>(transactionRepository.findTransactionsByUserGroupId(groupId));

            if (categories != null && !categories.isEmpty()) {
                transactionsSet.retainAll(transactionRepository.findTransactionByUserGroupIdAndCategoryIn(groupId, categories));
            }
            if (dateFrom != null && dateTo != null) {
                transactionsSet.retainAll(transactionRepository.findTransactionsByUserGroupIdAndDateBetween(groupId, dateFrom, dateTo));
            }

            if(members != null && !members.isEmpty()){
                transactionsSet.retainAll(transactionRepository.findMemberTransactionsByUserGroupId(groupId, members));
            }

            return transactionsSet.stream()
                    .map(transactionMapper::entityToReadGroupTransactionsResponse)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Optional<Transaction> read(Long transactionId, Long groupId) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty()) {
            throw new Exception("Transaction not found.");
        }
        if (!Objects.equals(transaction.get().getUserGroup().getId(), groupId)) {
            throw new Exception("Transaction does not belong to this group.");
        }
        return transaction;
    }

    public TransactionReadGroupTransactionResponse readGroupTransaction(Long transactionId, Long groupId) throws Exception {
        Transaction transaction = read(transactionId, groupId)
                .orElseThrow(() -> new Exception("Transaction not found"));
        return transactionMapper.entityToReadGroupTransactionResponse(transaction);
    }

    public List<Transaction> readAllByCategories(Long groupId, List<Category> categories) throws Exception {
        return transactionRepository.findTransactionByUserGroupIdAndCategoryIn(groupId, categories);
    }

    @Transactional
    public void update(Transaction transaction, TransactionUpdateRequest request, UserAccount nextPayer) throws Exception {
        try {
            amountService.deleteAllByTransactionId(transaction.getId());
            transaction.setName(request.getName());
            transaction.setDate(request.getDate());
            transaction.setAmount(request.getAmount());
            transaction.setPayer(nextPayer);

            List<Amount> amounts = amountService.setAmounts(request.getSpenderIds(), transaction);
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
        return transactionRepository.findTransactionsByUserGroupId(groupId);
    }

}
