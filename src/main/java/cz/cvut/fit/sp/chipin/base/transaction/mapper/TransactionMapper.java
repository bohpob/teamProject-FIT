package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserMapper;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import cz.cvut.fit.sp.chipin.base.amount.mapper.AmountMapper;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = {AmountService.class},
        uses = {AmountMapper.class, UserMapper.class}
)
public interface TransactionMapper {
    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    TransactionCreateTransactionResponse entityToCreateTransactionResponse(Transaction transaction);

    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    TransactionUpdateTransactionResponse entityToUpdateTransactionResponse(Transaction transaction);

    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    @Mapping(source = "payer.id", target = "payerId")
    @Mapping(source = "payer.firstName", target = "payerName")
    TransactionReadGroupTransactionResponse entityToReadGroupTransactionResponse(Transaction transaction);

    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    @Mapping(source = "payer.id", target = "payerId")
    @Mapping(source = "payer.firstName", target = "payerName")
    TransactionReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "payer", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "amounts", ignore = true)
    Transaction createTransactionRequestToEntity(TransactionCreateTransactionRequest transactionCreateTransactionRequest);
}
