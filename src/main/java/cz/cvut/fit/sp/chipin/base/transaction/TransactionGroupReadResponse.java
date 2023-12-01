package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserGroupReadResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class TransactionGroupReadResponse {
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
    @NotBlank
    private Float amount;
    @NotBlank
    private UserGroupReadResponse payer;
    @NotBlank
    private String date;
}
