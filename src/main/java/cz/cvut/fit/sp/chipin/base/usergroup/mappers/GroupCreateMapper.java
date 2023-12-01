package cz.cvut.fit.sp.chipin.base.usergroup.mappers;

import cz.cvut.fit.sp.chipin.base.usergroup.GroupCreateRequest;
import cz.cvut.fit.sp.chipin.base.usergroup.GroupCreateResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupCreateMapper {
    UserGroup requestToEntity(GroupCreateRequest groupCreateRequest);
    GroupCreateResponse entityToResponse(UserGroup userGroup);
}
