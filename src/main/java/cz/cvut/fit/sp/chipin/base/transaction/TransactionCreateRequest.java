package cz.cvut.fit.sp.chipin.base.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class TransactionCreateRequest {
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
    @NotNull
    private Long payerId;
    @NotNull
    private List<Long> spenderIds;
}
