package cz.cvut.fit.sp.chipin.base.usergroup.mapper;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.debt.mapper.DebtReadUserDebtsResponse;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadLogResponse;
import cz.cvut.fit.sp.chipin.base.member.mapper.MemberReadMemberResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface UserGroupMapper {

    GroupCreateGroupRequest entityToCreateGroupResponse(UserGroup userGroup);

    default GroupReadGroupResponse entityToReadGroupResponse(UserGroup userGroup){
        GroupReadGroupResponse response = new GroupReadGroupResponse();
        response.setName(userGroup.getName());
        response.setCurrency(userGroup.getCurrency().toString());
        response.setMembers(new ArrayList<>(userGroup.getMembers().stream().map(member -> {
            MemberReadMemberResponse memberReadMemberResponse = new MemberReadMemberResponse();
            memberReadMemberResponse.setName(member.getUserAccount().getUserEntity().getFirstName());
            memberReadMemberResponse.setUserRole(member.getRole().toString());
            return memberReadMemberResponse;
        }).toList()));
        response.setTransactions(entityToReadGroupTransactionsResponse(userGroup).transactions);
        response.setLogs(entityToReadGroupLogsResponse(userGroup).logs);
        response.setDebts(new ArrayList<>(userGroup.getDebts().stream().map(debt -> {
            DebtReadUserDebtsResponse debtReadUserDebtsResponse = new DebtReadUserDebtsResponse();
            debtReadUserDebtsResponse.setDebt(debt.getAmount());
            debtReadUserDebtsResponse.setBorrower(debt.getBorrower().getUserEntity().getFirstName());
            debtReadUserDebtsResponse.setLender(debt.getLender().getUserEntity().getFirstName());
            return debtReadUserDebtsResponse;
        }).toList()));
        return response;
    }
    @Mapping(target = "transactions", source = "transactions")
//    GroupReadGroupResponse entityToReadGroupResponse(UserGroup userGroup);

    GroupReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(UserGroup userGroup);

//    GroupReadGroupLogsResponse entityToReadGroupLogsResponse(UserGroup userGroup);
    default GroupReadGroupLogsResponse entityToReadGroupLogsResponse(UserGroup userGroup){
        GroupReadGroupLogsResponse response = new GroupReadGroupLogsResponse();
        response.setLogs(new ArrayList<>(userGroup.getLogs().stream().map(log -> {
            LogReadLogResponse logReadLogResponse = new LogReadLogResponse();
            logReadLogResponse.setAction(log.getAction().toString());
            logReadLogResponse.setDate(log.getDate().toString());
            logReadLogResponse.setUserName(log.getUserAccount().getUserEntity().getFirstName());
            return logReadLogResponse;
        }).toList()));
        return response;
    }

//    UserGroup createGroupRequestToEntity(GroupCreateGroupRequest groupCreateGroupRequest);

    default String transactionPayerToString(UserAccount userAccount) {
        return userAccount.getUserEntity().getFirstName();
    }

}
