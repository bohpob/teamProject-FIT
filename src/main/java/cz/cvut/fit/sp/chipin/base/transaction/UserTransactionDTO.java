package cz.cvut.fit.sp.chipin.base.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserTransactionDTO {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
}
