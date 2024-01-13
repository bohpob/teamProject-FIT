package cz.cvut.fit.sp.chipin.base.debt.mapper;

import lombok.Data;

import java.util.List;

@Data
public class UserReadUserDebtsResponse {
    List<DebtReadUserDebtsResponse> debts;
}
