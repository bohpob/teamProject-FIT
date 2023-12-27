package cz.cvut.fit.sp.chipin.base.amount.mapper;

import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {AmountService.class})
public interface AmountMapper {
    @Mapping(source = "user.name", target = "name")
    @Mapping(expression = "java(AmountService.roundAmount(amount.getAmount()))", target = "amount")
    AmountCreateAmountResponse entityToCreateAmountResponse(Amount amount);
}
