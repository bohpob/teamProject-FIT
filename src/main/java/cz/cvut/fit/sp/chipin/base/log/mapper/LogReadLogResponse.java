package cz.cvut.fit.sp.chipin.base.log.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogReadLogResponse {
    @NotBlank
    private String action;
    @NotBlank
    private String date;
    @NotBlank
    private String userName;
}
