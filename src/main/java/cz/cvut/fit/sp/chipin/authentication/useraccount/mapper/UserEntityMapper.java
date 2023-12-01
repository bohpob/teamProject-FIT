package cz.cvut.fit.sp.chipin.authentication.useraccount.mapper;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    @Mapping(target = "name", source = "firstName")
    UserReadUserResponse entityToReadUserResponse(UserEntity userEntity);
}
