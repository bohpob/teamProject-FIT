package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRepository;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountRepository;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.group.GroupRepository;
import cz.cvut.fit.sp.chipin.base.group.GroupService;
import cz.cvut.fit.sp.chipin.base.membership.MembershipRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class TransactionService {
    private final GroupService groupService;
    private final TransactionRepository transactionRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final AmountRepository amountRepository;
    private final MembershipRepository membershipRepository;

    public ResponseEntity<TransactionDTO> create(TransactionCreateRequest transactionDTO, Long group_id) throws Exception {
        Optional<Group> group = groupRepository.findById(group_id);
        if (group.isEmpty())
            throw new Exception("Group not found.");
        Optional<User> payer = userRepository.findById(transactionDTO.getPayerId());
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        Transaction transaction = TransactionConverter.fromDto(transactionDTO, group.get(), payer.get());

        try {
            setAmounts(transactionDTO.getSpenderIds(), transaction, group_id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        groupService.acceptTxCreate(transaction);

        return ResponseEntity.ok(TransactionConverter.toDto(transaction));
    }

    private void setAmounts(List<Long> spenderIds, Transaction transaction, Long groupId) throws Exception {
        if (spenderIds.isEmpty())
            throw new Exception("Users not found.");

        List<Amount> amounts = new ArrayList<>();
        Float spent = transaction.getAmount() / spenderIds.size();

        for (Long id : spenderIds) {
            if (membershipRepository.findByUserIdAndGroupId(id, groupId).isPresent()) {
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
        transactionRepository.save(transaction);
        amountRepository.saveAll(amounts);
    }

    public ResponseEntity<TransactionDTO> read(Long transaction_id, Long group_id) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transaction_id);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        if (!Objects.equals(transaction.get().getGroup().getId(), group_id))
            throw new Exception("Transaction does not belong to this group.");
        return ResponseEntity.ok(TransactionConverter.toDto(transaction.get()));
    }

    public void delete(Long transaction_id, Long group_id) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(transaction_id);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        if (!Objects.equals(transaction.get().getGroup().getId(), group_id))
            throw new Exception("Transaction does not belong to this group.");
        transactionRepository.deleteById(transaction_id);
    }
}
