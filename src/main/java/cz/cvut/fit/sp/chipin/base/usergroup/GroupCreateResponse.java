package cz.cvut.fit.sp.chipin.base.usergroup;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccountGroupResponse;
import cz.cvut.fit.sp.chipin.base.debt.DebtGroupResponse;
import cz.cvut.fit.sp.chipin.base.log.LogDTO;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionGroupResponse;
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
public class GroupCreateResponse {
    @NotBlank
    private String name;
    @NotNull
    private Currency currency;
    @NotNull
    private List<LogDTO> logs;
}

