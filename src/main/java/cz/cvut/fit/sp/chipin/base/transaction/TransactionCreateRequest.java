package cz.cvut.fit.sp.chipin.base.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TransactionCreateRequest {
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
    @NotNull
    private String payerId;
    @NotNull
    private List<String> spenderIds;
}
