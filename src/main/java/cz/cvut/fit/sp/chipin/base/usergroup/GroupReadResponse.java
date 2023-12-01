package cz.cvut.fit.sp.chipin.base.usergroup;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserGroupReadResponse;
import cz.cvut.fit.sp.chipin.base.debt.DebtGroupReadResponse;
import cz.cvut.fit.sp.chipin.base.log.LogGroupReadResponse;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionGroupReadResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GroupReadResponse {

    @NotBlank
    private String name;
    @NotNull
    private Currency currency;
    @NotNull
    private List<UserGroupReadResponse> userAccounts;
    @NotNull
    private List<TransactionGroupReadResponse> transactions;
    @NotNull
    private List<DebtGroupReadResponse> debts;
    @NotNull
    private List<LogGroupReadResponse> logs;
}

