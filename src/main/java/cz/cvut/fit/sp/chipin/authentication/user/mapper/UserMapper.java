package cz.cvut.fit.sp.chipin.authentication.user.mapper;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface UserMapper {
    UserReadUserResponse entityToReadUserResponse(User user);

    UserReadUserTransactionsResponse entityToReadUserTransactionsResponse(User user);

    UserCreateTransactionResponse entityToCreateTransactionResponse(User user);
}
