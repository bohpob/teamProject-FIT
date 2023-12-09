package cz.cvut.fit.sp.chipin.base.group.mapper;

import cz.cvut.fit.sp.chipin.base.debt.mapper.DebtMapper;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogMapper;
import cz.cvut.fit.sp.chipin.base.member.mapper.MemberMapper;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import cz.cvut.fit.sp.chipin.base.group.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {DebtMapper.class, MemberMapper.class, TransactionMapper.class, LogMapper.class}
)
public interface GroupMapper {
    GroupCreateGroupResponse entityToCreateGroupResponse(Group group);

    GroupReadGroupResponse entityToReadGroupResponse(Group group);

    @Mapping(source = "group.transactions", target = "transactions")
    GroupReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(Group group);

    @Mapping(source = "group.logs", target = "logs")
    GroupReadGroupLogsResponse entityToReadGroupLogsResponse(Group group);

    GroupReadGroupTransactionsResponse transactionsToReadGroupTransactionsResponse(Integer dummy, List<Transaction> transactions);

    GroupUpdateGroupNameResponse entityToUpdateGroupNameResponse(Group group);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "debts", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "hexCode", ignore = true)
    Group createGroupRequestToEntity(GroupCreateGroupRequest groupCreateGroupRequest);

    GroupReadGroupMembersResponse entityToReadGroupMembersResponse(Group group);
}
