package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.member.mapper.MemberReadMemberResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionCreateTransactionRequest;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Page<TransactionReadGroupTransactionsResponse> readGroupTransactions(
            Long groupId, TransactionReadGroupTransactionsSmartRequest request, Pageable pageable) throws Exception {
        try {
            Stream<Transaction> transactions = transactionRepository.findTransactionsByGroupId(groupId).stream();

            if (!request.categories.isEmpty()) {
                transactions = transactions.filter(transaction ->
                        request.categories.contains(transaction.getCategory().name())
                );
            }

            if (!request.getDateTimeFrom().isBlank() && !request.getDateTimeTo().isBlank()) {
                LocalDateTime dateTimeFrom = parseDateTime(request.getDateTimeFrom());
                LocalDateTime dateTimeTo = parseDateTime(request.getDateTimeTo());
                transactions = transactions.filter(transaction ->
                        !transaction.getDateTime().isBefore(dateTimeFrom) &&
                                !transaction.getDateTime().isAfter(dateTimeTo)
                );
            }

            if (!request.getMembers().isEmpty()) {
                List<String> memberIds = request.getMembers().stream().map(MemberReadMemberResponse::getId).toList();
                transactions = transactions.filter(transaction -> memberIds.contains(transaction.getPayer().getId()));
            }

            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;
            List<TransactionReadGroupTransactionsResponse> pageContent;
            List<TransactionReadGroupTransactionsResponse> groupResponses = transactions
                    .map(transactionMapper::entityToReadGroupTransactionsResponse)
                    .collect(Collectors.toList());
            if (groupResponses.size() < startItem) {
                pageContent = new ArrayList<>();
            } else {
                int toIndex = Math.min(startItem + pageSize, groupResponses.size());
                pageContent = groupResponses.subList(startItem, toIndex);
            }

            return new PageImpl<>(pageContent, pageable, groupResponses.size());

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

    public Page<Transaction> readAllByCategories(Long groupId, List<Category> categories, Pageable pageable) throws Exception {
        return transactionRepository.findTransactionByGroupIdAndCategoryIn(groupId, categories, pageable);
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

    public Page<Transaction> readTransactions(Long groupId, Pageable pageable) {
        return transactionRepository.findTransactionsByGroupId(groupId, pageable);
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TransactionMapper.DATETIME_FORMAT);
        return LocalDateTime.parse(dateTime, formatter);
    }
}
