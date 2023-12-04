package cz.cvut.fit.sp.chipin.base.usergroup.mapper;

import cz.cvut.fit.sp.chipin.base.debt.mapper.DebtMapper;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogMapper;
import cz.cvut.fit.sp.chipin.base.member.mapper.MemberMapper;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {DebtMapper.class, MemberMapper.class, TransactionMapper.class, LogMapper.class}
)
public interface UserGroupMapper {
    GroupCreateGroupRequest entityToCreateGroupResponse(UserGroup userGroup);

    GroupReadGroupResponse entityToReadGroupResponse(UserGroup userGroup);

    GroupReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(UserGroup userGroup);

    GroupReadGroupLogsResponse entityToReadGroupLogsResponse(UserGroup userGroup);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "debts", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "hexCode", ignore = true)
    UserGroup createGroupRequestToEntity(GroupCreateGroupRequest groupCreateGroupRequest);
}
