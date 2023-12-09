package cz.cvut.fit.sp.chipin.authentication.user.mapper;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "firstName", target = "name")
    @Mapping(source = "email", target = "email")
    UserReadUserResponse entityToReadUserResponse(User user);

    UserReadUserTransactionsResponse entityToReadUserTransactionsResponse(User user);
}
