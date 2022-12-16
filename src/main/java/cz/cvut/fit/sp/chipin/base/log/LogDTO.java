package cz.cvut.fit.sp.chipin.base.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LogDTO {
    @NotBlank
    private String action;
    @NotBlank
    private String date;
    @NotBlank
    private String userName;
}