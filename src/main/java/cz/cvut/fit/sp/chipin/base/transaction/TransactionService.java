package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRepository;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountRepository;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.group.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final AmountRepository amountRepository;

    public ResponseEntity<TransactionDTO> create(TransactionDTO transactionDTO, Long group_id) throws Exception {
        Optional<Group> group = groupRepository.findById(group_id);
        if (group.isEmpty())
            throw new Exception("Group not found.");
        Optional<User> payer = userRepository.findById(transactionDTO.getPayerId());
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        Transaction transaction = TransactionConverter.fromDto(transactionDTO, group.get(), payer.get());
        Map<User, Float> spent;

        try {
            spent = setAmountsAndSpent(transactionDTO.getUserIds(), transaction.getPayer().getId(), transaction);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        //group.recalculate(spent, transaction.getPayer().getId(), transaction.getAmount());

        return ResponseEntity.ok(transactionDTO);
    }

    private Map<User, Float> setAmountsAndSpent(List<Long> userIds, Long payerId, Transaction transaction) throws Exception {
        if (userIds.isEmpty())
            throw new Exception("Users not found.");

        List<Amount> amounts = new ArrayList<>();
        Map<User, Float> spent = new HashMap<>();
        Float spentPerUser = transaction.getAmount() / userIds.size();

        for (Long id : userIds) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                Float userAmount = ((Objects.equals(id, payerId)) ? transaction.getAmount() : 0f);
                amounts.add(new Amount(user.get(), transaction, userAmount));
                spent.put(user.get(), spentPerUser);
            } else {
                throw new Exception("User not found.");
            }
        }

        transactionRepository.save(transaction);
        amountRepository.saveAll(amounts);
        return spent;
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
