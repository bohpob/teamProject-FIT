package cz.cvut.fit.sp.chipin.base.usergroup.mappers;

import cz.cvut.fit.sp.chipin.authentication.useraccount.mapper.UserReadGroupResponse;
import cz.cvut.fit.sp.chipin.base.debt.mapper.DebtReadGroupResponse;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadGroupResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mappers.TransactionReadGroupResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class GroupReadGroupResponse {

    @NotBlank
    private String name;
    @NotNull
    private String currency;
    @NotNull
    private List<MemberReadMemberResponse> members;
    @NotNull
    private List<TransactionReadGroupResponse> transactions;
    @NotNull
    private List<DebtReadGroupResponse> debts;
    @NotNull
    private List<LogReadGroupResponse> logs;
}
