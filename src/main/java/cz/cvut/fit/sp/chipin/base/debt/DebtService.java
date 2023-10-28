package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DebtService {
    private final DebtRepository debtRepository;

    private void checkingForEmptyOrNegativeDebt(Debt debt, UserGroup userGroup, UserAccount spender, UserAccount payer) throws Exception {
        if (debt.getAmount() == 0) {
            debtRepository.deleteById(debt.getId());
        } else if (debt.getAmount() < 0) {
            Float newAmount = debt.getAmount() * (-1);
            debtRepository.deleteById(debt.getId());
            Debt newDebt = new Debt(userGroup, spender, payer, newAmount);
            debtRepository.save(newDebt);
        }
    }

    public void recalculate(Map<UserAccount, Float> spent, UserAccount payer, UserGroup userGroup) throws Exception {
        for (var entry : spent.entrySet()) {
            UserAccount spender = entry.getKey();
            if (Objects.equals(spender.getId(), payer.getId())) {
                continue;
            }

            Optional<Debt> debt = debtRepository.findByUserGroupIdAndLenderIdAndBorrowerId(userGroup.getId(),
                    payer.getId(), spender.getId());

            if (debt.isEmpty()) {
                debt = debtRepository.findByUserGroupIdAndLenderIdAndBorrowerId(userGroup.getId(),
                        spender.getId(), payer.getId());

                if (debt.isEmpty()) {
                    Debt newDebt = new Debt(userGroup, payer, spender, entry.getValue());
                    debtRepository.save(newDebt);
                    checkingForEmptyOrNegativeDebt(newDebt, userGroup, spender, payer);
                } else {
                    debt.get().setAmount(debt.get().getAmount() - entry.getValue());
                    debtRepository.save(debt.get());
                    checkingForEmptyOrNegativeDebt(debt.get(), userGroup, payer, spender);
                }
            } else {
                debt.get().setAmount(debt.get().getAmount() + entry.getValue());
                debtRepository.save(debt.get());
                checkingForEmptyOrNegativeDebt(debt.get(), userGroup, spender, payer);
            }
        }
    }

    public List<Debt> readDebts(Long groupId) throws Exception {
        return debtRepository.findDebtsByUserGroupId(groupId);
    }

    public Optional<Debt> readDebt(Long groupId, String lenderId, String borrowerId) {
        return debtRepository.findByUserGroupIdAndLenderIdAndBorrowerId(groupId, lenderId, borrowerId);
    }
}
