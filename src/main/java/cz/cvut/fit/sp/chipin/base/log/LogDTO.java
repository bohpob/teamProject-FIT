package cz.cvut.fit.sp.chipin.base.log;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogDTO {
    @NotBlank
    private String action;
    @NotBlank
    private String date;
    @NotBlank
    private String userAccountName;
}