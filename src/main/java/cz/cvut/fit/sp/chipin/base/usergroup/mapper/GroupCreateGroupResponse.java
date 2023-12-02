package cz.cvut.fit.sp.chipin.base.usergroup.mapper;

import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadLogResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupCreateGroupResponse {
    @NotBlank
    private String name;
    @NotNull
    private String currency;
    @NotNull
    private LogReadLogResponse log;
}
