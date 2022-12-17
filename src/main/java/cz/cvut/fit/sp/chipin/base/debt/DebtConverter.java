package cz.cvut.fit.sp.chipin.base.debt;

public class DebtConverter {
    public static DebtGroupResponse toDebtGroupResponse(Debt debt) {
        return new DebtGroupResponse(debt.getLender().getName(), debt.getBorrower().getName(), debt.getAmount());
    }
}
