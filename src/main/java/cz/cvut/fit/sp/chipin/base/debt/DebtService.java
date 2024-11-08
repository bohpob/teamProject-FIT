package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.notification.NotificationService;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
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
    private final NotificationService notificationService;

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
            if (Objects.equals(spender.getId(), payer.getId())) {
                continue;
            }

            Optional<Debt> debt = debtRepository.findByGroupIdAndLenderIdAndBorrowerId(group.getId(),
                    payer.getId(), spender.getId());

            if (debt.isEmpty()) {
                debt = debtRepository.findByGroupIdAndLenderIdAndBorrowerId(group.getId(),
                        spender.getId(), payer.getId());

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

    public void notifyDebtRepayment(User borrower, User lender, Group group, Float debtAmount) {
        notificationService.createNotification(borrower, group,
                new NotificationContent("Debt Repaid", "You repaid a debt of $" + debtAmount +
                                " to " + lender.getName()));

        // Notifying the lender of debt settlement
        notificationService.createNotification(lender, group,
                new NotificationContent("Debt Settled", borrower.getName() + " repaid a debt of $" +
                                debtAmount + " to you"));
    }

    public List<Debt> readDebts(Long groupId) throws Exception {
        return debtRepository.findDebtsByGroupId(groupId);
    }

    public Optional<Debt> readDebt(Long groupId, String lenderId, String borrowerId) {
        return debtRepository.findByGroupIdAndLenderIdAndBorrowerId(groupId, lenderId, borrowerId);
    }
}
