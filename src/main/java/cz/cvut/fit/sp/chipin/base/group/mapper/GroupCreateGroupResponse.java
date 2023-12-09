package cz.cvut.fit.sp.chipin.base.group.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupCreateGroupResponse {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
}
