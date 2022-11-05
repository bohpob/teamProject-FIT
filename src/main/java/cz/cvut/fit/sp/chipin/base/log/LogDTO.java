package cz.cvut.fit.sp.chipin.base.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LogDTO {
    @NotNull
    @NotBlank
    private String action;
    @NotNull
    @NotBlank
    private String date;
    @NotNull
    @NotBlank
    private String userName;
}