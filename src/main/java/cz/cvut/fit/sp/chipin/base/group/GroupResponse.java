package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.user.UserGroupResponse;
import cz.cvut.fit.sp.chipin.base.debt.DebtGroupResponse;
import cz.cvut.fit.sp.chipin.base.log.LogDTO;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionGroupResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    @NotBlank
    private String name;
    @NotNull
    private Currency currency;
    @NotNull
    private List<UserGroupResponse> users;
    @NotNull
    private List<TransactionGroupResponse> transactions;
    @NotNull
    private List<DebtGroupResponse> debts;
    @NotNull
    private List<LogDTO> logs;
}
