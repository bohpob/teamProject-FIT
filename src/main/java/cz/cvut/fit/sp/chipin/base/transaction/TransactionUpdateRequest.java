package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionUpdateRequest {
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
    @NotBlank
    private String dateTime;
    @NotNull
    private String payerId;
    @NotNull
    private TransactionType splitStrategy;
    @NotNull
    private List<MemberAbstractRequest> spenders;
}
