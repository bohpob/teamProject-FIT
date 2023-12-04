package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Locale;

@Mapper(componentModel = "spring", imports = Locale.class)
public interface TransactionMapper {
    @Mapping(
            expression = "java(Float.valueOf(String.format(Locale.US, \"%.2f\", transaction.getAmount())))",
            target = "amount"
    )
    @Mapping(source = "payer.userEntity.firstName", target = "payer")
    TransactionReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(Transaction transaction);
}
