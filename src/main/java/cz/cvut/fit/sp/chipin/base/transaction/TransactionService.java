package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionCreateTransactionRequest;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AmountService amountService;
    private final TransactionMapper transactionMapper;

    public Transaction create(TransactionCreateTransactionRequest request, User payer, Group group) throws Exception {
        Transaction transaction = transactionMapper.createTransactionRequestToEntity(request);
        transaction.setPayer(payer);
        transaction.setGroup(group);
        transaction.setDateTime(LocalDateTime.now());
        //TODO: replace with Request field
        transaction.setCategory(Category.NO_CATEGORY);

        try {
            List<Amount> amounts = amountService.setAmounts(transaction, request.getSpenders(), request.getSplitStrategy());
            transactionRepository.save(transaction);
            amountService.saveAll(amounts);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return transaction;
    }

    public List<TransactionReadGroupTransactionsResponse> readGroupTransactions(
            Long groupId, TransactionReadGroupTransactionsSmartRequest request) throws Exception {
        try {

//            List<Category> categories = request.getCategories().stream().map(Category::valueOf).collect(Collectors.toList());
//            List<Member> members = memberService.readMembers(groupId).stream()
//                    .filter(member -> request.getMembers().stream()
//                    .anyMatch(memberReadMemberResponse -> memberReadMemberResponse.getId().equals(member.getId().getUserAccountId()))).toList();
//            String dateFrom = request.getDateFrom();
//            String dateTo = request.getDateTo();

            Set<Transaction> transactionsSet = new HashSet<>(transactionRepository.findTransactionsByGroupId(groupId));

            if (!request.categories.isEmpty()) {
//                transactionsSet.retainAll(transactionRepository.findTransactionByUserGroupIdAndCategoryIn(groupId, categories));
                transactionsSet = transactionsSet.stream()
                        .filter(transaction -> request.categories.contains(transaction.getCategory().name()))
                        .collect(Collectors.toSet());
            }
            if (!request.dateTimeFrom.isBlank() && !request.dateTimeTo.isBlank()) {
//                transactionsSet.retainAll(transactionRepository.findTransactionsByUserGroupIdAndDateBetween(groupId, dateFrom, dateTo));
                transactionsSet = transactionsSet.stream()
                        .filter(transaction -> {
                            LocalDateTime dateTimeFrom = parseDateTime(request.getDateTimeFrom());
                            LocalDateTime dateTimeTo = parseDateTime(request.getDateTimeTo());
                            return !transaction.getDateTime().isBefore(dateTimeFrom) &&
                                    !transaction.getDateTime().isAfter(dateTimeTo);
                        })
                        .collect(Collectors.toSet());
            }

            if (!request.getMembers().isEmpty()) {
                transactionsSet = transactionsSet.stream()
                        .filter(transaction -> request.getMembers().stream()
                                .anyMatch(memberReadMemberResponse -> memberReadMemberResponse.getId()
                                        .equals(transaction.getPayer().getId())))
                        .collect(Collectors.toSet());
            }

            return transactionsSet.stream()
                    .map(transactionMapper::entityToReadGroupTransactionsResponse).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
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

    public List<Transaction> readAllByCategories(Long groupId, List<Category> categories) throws Exception {
        return transactionRepository.findTransactionByGroupIdAndCategoryIn(groupId, categories);
    }

    @Transactional
    public void update(Transaction transaction, TransactionUpdateRequest request, User nextPayer) throws Exception {
        try {
            amountService.deleteAllByTransactionId(transaction.getId());
            transaction.setName(request.getName());
            transaction.setDateTime(parseDateTime(request.getDateTime()));
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

    private static LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TransactionMapper.DATETIME_FORMAT);
        return LocalDateTime.parse(dateTime, formatter);
    }
}
