package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Locale;

@Mapper(componentModel = "spring", imports = Locale.class)
public interface TransactionMapper {

    @Mapping(expression = "java(Float.valueOf(String.format(Locale.US, \"%.2f\", transaction.getAmount())))", target = "amount")
    @Mapping( target = "payer", source = "payer.userEntity.firstName")
    TransactionReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(Transaction transaction);

    List<TransactionReadGroupTransactionsResponse> entitiesToReadGroupTransactionsResponse(List<Transaction> transactions);

}
