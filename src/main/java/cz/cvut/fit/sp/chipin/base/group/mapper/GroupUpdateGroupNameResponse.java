package cz.cvut.fit.sp.chipin.base.group.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupUpdateGroupNameResponse {
    @NotBlank
    private String name;
    @NotNull
    private String currency;
}
