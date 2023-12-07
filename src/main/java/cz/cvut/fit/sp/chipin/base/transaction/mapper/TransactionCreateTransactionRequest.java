package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class TransactionCreateTransactionRequest {
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
    @NotBlank
    private String currency;
    @NotBlank
    private String payerId;
    @NotNull
    private TransactionType splitStrategy;
    @NotNull
    private List<MemberAbstractRequest> spenders;
}