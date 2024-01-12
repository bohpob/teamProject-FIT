package cz.cvut.fit.sp.chipin.base.group.mapper;

import cz.cvut.fit.sp.chipin.base.debt.mapper.DebtReadUserDebtsResponse;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadLogResponse;
import cz.cvut.fit.sp.chipin.base.member.mapper.MemberReadMemberResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupReadGroupResponse {

    @NotBlank
    private String name;
    @NotBlank
    private String currency;
    @NotBlank
    private String nextPayerId;
    @NotBlank
    private String payerStrategy;
    @NotNull
    private Boolean checkNextPayer;
    @NotNull
    private List<MemberReadMemberResponse> members;
    @NotNull
    private List<TransactionReadGroupTransactionsResponse> transactions;
    @NotNull
    private List<DebtReadUserDebtsResponse> debts;
    @NotNull
    private List<LogReadLogResponse> logs;
}
