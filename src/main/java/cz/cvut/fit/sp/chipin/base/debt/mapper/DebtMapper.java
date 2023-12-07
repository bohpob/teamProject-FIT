package cz.cvut.fit.sp.chipin.base.debt.mapper;

import cz.cvut.fit.sp.chipin.base.debt.Debt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Locale;

@Mapper(componentModel = "spring", imports = Locale.class)
public interface DebtMapper {
    @Mapping(source = "lender.id", target = "lenderId")
    @Mapping(source = "lender.firstName", target = "lenderName")
    @Mapping(source = "borrower.id", target = "borrowerId")
    @Mapping(source = "borrower.firstName", target = "borrowerName")
    @Mapping(expression = "java(Float.valueOf(String.format(Locale.US, \"%.2f\", debt.getAmount())))", target = "debt")
    DebtReadUserDebtsResponse entityToReadUserDebtsResponse(Debt debt);

    UserReadUserDebtsResponse entitiesToReadUserDebtsResponse(Integer dummy, List<DebtReadUserDebtsResponse> debts);
}
