package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import cz.cvut.fit.sp.chipin.base.amount.mapper.AmountMapper;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {AmountService.class}, uses = {AmountMapper.class})
public interface TransactionMapper {
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    @Mapping(source = "firstName", target = "name")
    UserCreateTransactionResponse userEntityToCreateTransactionResponse(User user);

    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    @Mapping(target = "dateTime", dateFormat = DATETIME_FORMAT)
    TransactionCreateTransactionResponse entityToCreateTransactionResponse(Transaction transaction);

    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    @Mapping(target = "dateTime", dateFormat = DATETIME_FORMAT)
    TransactionUpdateTransactionResponse entityToUpdateTransactionResponse(Transaction transaction);

    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    @Mapping(source = "payer.id", target = "payerId")
    @Mapping(source = "payer.firstName", target = "payerName")
    @Mapping(target = "dateTime", dateFormat = DATETIME_FORMAT)
    TransactionReadGroupTransactionResponse entityToReadGroupTransactionResponse(Transaction transaction);

    @Mapping(expression = "java(AmountService.roundAmount(transaction.getAmount()))", target = "amount")
    @Mapping(source = "payer.id", target = "payerId")
    @Mapping(source = "payer.firstName", target = "payerName")
    @Mapping(target = "dateTime", dateFormat = DATETIME_FORMAT)
    TransactionReadGroupTransactionsResponse entityToReadGroupTransactionsResponse(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "payer", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "amounts", ignore = true)
    Transaction createTransactionRequestToEntity(TransactionCreateTransactionRequest transactionCreateTransactionRequest);
}
