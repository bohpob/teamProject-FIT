package cz.cvut.fit.sp.chipin.base.usergroup.mappers;

import cz.cvut.fit.sp.chipin.base.usergroup.GroupReadResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupReadMapper {
    GroupReadResponse entityToResponse(UserGroup userGroup);
    List<GroupReadResponse> entitiesToResponses(List<UserGroup> userGroups);
}
