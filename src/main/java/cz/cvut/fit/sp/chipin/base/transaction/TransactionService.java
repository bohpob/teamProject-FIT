package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRepository;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountRepository;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.group.GroupRepository;
import cz.cvut.fit.sp.chipin.base.group.GroupService;
import cz.cvut.fit.sp.chipin.base.log.LogService;
import cz.cvut.fit.sp.chipin.base.membership.Member;
import cz.cvut.fit.sp.chipin.base.membership.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class TransactionService {
    private final GroupService groupService;
    private final LogService logService;
    private final TransactionRepository transactionRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final AmountRepository amountRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<TransactionDTO> create(TransactionCreateRequest transactionCreateRequest, Long group_id) throws Exception {
        Optional<Group> group = groupRepository.findById(group_id);
        if (group.isEmpty())
            throw new Exception("Group not found.");
        Optional<Member> payer = memberRepository.findByUserIdAndGroupId(transactionCreateRequest.getPayerId(), group_id);
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        Transaction transaction = TransactionConverter.fromCreateDto(transactionCreateRequest, payer.get());

        try {
            List<Amount> amounts = setAmounts(transactionCreateRequest.getSpenderIds(), transaction, group_id);
            transactionRepository.save(transaction);
            amountRepository.saveAll(amounts);
            groupService.acceptTxCreate(transaction);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        // change to the user who will actually delete the transaction
        logService.create("made a payment: " + transaction.getAmount(), transaction.getPayer().getGroup(), transaction.getPayer().getUser());
        return ResponseEntity.ok(TransactionConverter.toDto(transaction));
    }

    private List<Amount> setAmounts(List<Long> spenderIds, Transaction transaction, Long groupId) throws Exception {
        if (spenderIds.isEmpty())
            throw new Exception("Users not found.");

        List<Amount> amounts = new ArrayList<>();
        Float spent = transaction.getAmount() / spenderIds.size();

        for (Long id : spenderIds) {
            if (memberRepository.findByUserIdAndGroupId(id, groupId).isPresent()) {
                Optional<User> user = userRepository.findById(id);
                if (user.isPresent()) {
                    amounts.add(new Amount(user.get(), transaction, spent));
                } else {
                    throw new Exception("User not found.");
                }
            } else {
                throw new Exception("User is not from this group");
            }
        }
        transaction.setAmounts(amounts);
        return amounts;
    }

    public ResponseEntity<TransactionDTO> read(Long transaction_id, Long group_id) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transaction_id);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        if (!Objects.equals(transaction.get().getPayer().getGroup().getId(), group_id))
            throw new Exception("Transaction does not belong to this group.");
        return ResponseEntity.ok(TransactionConverter.toDto(transaction.get()));
    }

    @Transactional
    public void updateTransaction(Transaction transaction, TransactionUpdateRequest transactionUpdateRequest, Member nextPayer, Long group_id) throws Exception {
        transaction.setName(transactionUpdateRequest.getName());
        transaction.setDate(transactionUpdateRequest.getDate());
        transaction.setAmount(transactionUpdateRequest.getAmount());
        transaction.setPayer(nextPayer);

        try {
            List<Amount> amounts = setAmounts(transactionUpdateRequest.getSpenderIds(), transaction, group_id);
            amountRepository.saveAll(amounts);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<TransactionDTO> update(TransactionUpdateRequest transactionUpdateRequest, Long group_id, Long transaction_id) throws Exception {
        Optional<Group> group = groupRepository.findById(group_id);
        if (group.isEmpty())
            throw new Exception("Group not found.");
        Optional<Transaction> transaction = transactionRepository.findById(transaction_id);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        if (!Objects.equals(transaction.get().getPayer().getGroup().getId(), group_id))
            throw new Exception("Transaction does not belong to this group.");
        Optional<Member> prevPayer = memberRepository.findByUserIdAndGroupId(transaction.get().getPayer().getUser().getId(), group_id);
        if (prevPayer.isEmpty())
            throw new Exception("Payer not found.");
        Optional<Member> nextPayer = memberRepository.findByUserIdAndGroupId(transactionUpdateRequest.getPayerId(), group_id);
        if (nextPayer.isEmpty())
            throw new Exception("Payer not found.");

        groupService.acceptTxDelete(transaction.get());
        updateTransaction(transaction.get(), transactionUpdateRequest, nextPayer.get(), group_id);
        groupService.acceptTxCreate(transaction.get());

        return ResponseEntity.ok(TransactionConverter.toDto(transaction.get()));
    }

    public void delete(Long transaction_id, Long group_id) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transaction_id);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        Optional<Member> payer = memberRepository.findByUserIdAndGroupId(transaction.get().getPayer().getUser().getId(), group_id);
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        groupService.acceptTxDelete(transaction.get());
        // change to the user who will actually delete the transaction
        logService.create("deleted transaction", payer.get().getGroup(), payer.get().getUser());
        transactionRepository.deleteById(transaction_id);
    }
}
