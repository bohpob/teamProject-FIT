package cz.cvut.fit.sp.chipin.authentication.user.mapper;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface UserMapper {
    @Mapping(source = "firstName", target = "name")
    UserReadUserResponse entityToReadUserResponse(User user);

    UserReadUserTransactionsResponse entityToReadUserTransactionsResponse(User user);
}
