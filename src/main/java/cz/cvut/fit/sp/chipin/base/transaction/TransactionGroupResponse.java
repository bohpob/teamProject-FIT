package cz.cvut.fit.sp.chipin.base.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@Getter
@AllArgsConstructor
public class TransactionGroupResponse {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
    @NotBlank
    private String date;
    @NotBlank
    private String payer;
    @NotNull
    private List<String> spenders;
}
