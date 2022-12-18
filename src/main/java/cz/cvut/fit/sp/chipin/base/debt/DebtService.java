package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionCreateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class DebtService {

    private final DebtRepository debtRepository;
    private final TransactionService transactionService;

    private void checkingForEmptyOrNegativeDebt(Debt debt, Group group, User spender, User payer) throws Exception {
        if (debt.getAmount() == 0) {
            debtRepository.deleteById(debt.getId());
        } else if (debt.getAmount() < 0) {
            Float newAmount = debt.getAmount() * (-1);
            debtRepository.deleteById(debt.getId());
            Debt newDebt = new Debt(group, spender, payer, newAmount);
            debtRepository.save(newDebt);
        }
    }

    public void recalculate(Map<User, Float> spent, User payer, Group group) throws Exception {
        for (var entry : spent.entrySet()) {
            User spender = entry.getKey();
            if (Objects.equals(spender.getId(), payer.getId()))
                continue;

            Optional<Debt> debt = debtRepository.findByGroupIdAndLenderIdAndBorrowerId(group.getId(), payer.getId(), spender.getId());
            if (debt.isEmpty()) {
                debt = debtRepository.findByGroupIdAndLenderIdAndBorrowerId(group.getId(), spender.getId(), payer.getId());
                if (debt.isEmpty()) {
                    Debt newDebt = new Debt(group, payer, spender, entry.getValue());
                    debtRepository.save(newDebt);
                    checkingForEmptyOrNegativeDebt(newDebt, group, spender, payer);
                } else {
                    debt.get().setAmount(debt.get().getAmount() - entry.getValue());
                    debtRepository.save(debt.get());
                    checkingForEmptyOrNegativeDebt(debt.get(), group, payer, spender);
                }
            } else {
                debt.get().setAmount(debt.get().getAmount() + entry.getValue());
                debtRepository.save(debt.get());
                checkingForEmptyOrNegativeDebt(debt.get(), group, spender, payer);
            }
        }
    }

    public Transaction delete(Long groupId, DebtKeyDTO dto, Member lender, Member borrower) throws Exception {
        Optional<Debt> debt = getDebt(groupId, dto.getLenderId(), dto.getBorrowerId());
        if (debt.isEmpty())
            throw new Exception("Debt not found");

        List<Long> lenderId = new ArrayList<>();
        lenderId.add(dto.getLenderId());
        TransactionCreateRequest transactionCreateRequest = new TransactionCreateRequest(
                borrower.getUser().getName() + " repaid " + lender.getUser().getName() + "'s " + "debt"
                , debt.get().getAmount(), borrower.getUser().getId(), lenderId);

        return transactionService.create(transactionCreateRequest, borrower, groupId);
    }

    public List<Debt> getDebtsByGroupId(Long groupId) throws Exception {
        return debtRepository.findDebtsByGroupId(groupId);
    }

    public Optional<Debt> getDebt(Long groupId, Long lenderId, Long borrowerId) {
        return debtRepository.findByGroupIdAndLenderIdAndBorrowerId(groupId, lenderId, borrowerId);
    }
}
