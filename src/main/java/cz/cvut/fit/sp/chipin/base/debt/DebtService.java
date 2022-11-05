package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
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
                    continue;
                }

                debt.get().setAmount(debt.get().getAmount() - entry.getValue());
                if (debt.get().getAmount() == 0) {
                    debtRepository.deleteById(debt.get().getId());
                    continue;
                } else if (debt.get().getAmount() < 0) {
                    Float newAmount = debt.get().getAmount() * (-1);
                    debtRepository.deleteById(debt.get().getId());

                    Debt newDebt = new Debt(group, payer, spender, newAmount);
                    debtRepository.save(newDebt);
                    continue;
                }
            }

            debt.get().setAmount(debt.get().getAmount() + entry.getValue());
        }
    }
}
