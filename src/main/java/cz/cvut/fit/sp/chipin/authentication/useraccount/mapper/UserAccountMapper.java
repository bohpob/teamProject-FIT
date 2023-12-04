package cz.cvut.fit.sp.chipin.authentication.useraccount.mapper;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    @Mapping(source = "userEntity.firstName", target = "name")
    @Mapping(source = "userEntity.email", target = "email")
    UserReadUserResponse entityToReadUserResponse(UserAccount userAccount);
}
