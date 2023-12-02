package cz.cvut.fit.sp.chipin.base.usergroup.mapper;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserGroupMapper {
    GroupCreateGroupRequest entityToCreateGroupResponse(UserGroup userGroup);

    GroupReadGroupResponse entityToReadGroupResponse(UserGroup userGroup);

    GroupReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(UserGroup userGroup);

    GroupReadGroupLogsResponse entityToReadGroupLogsResponse(UserGroup userGroup);

    UserGroup createGroupRequestToEntity(GroupCreateGroupRequest groupCreateGroupRequest);

    default String userAccountToString(UserAccount userAccount) {
        return userAccount.getUserEntity().getFirstName();
    }
}
