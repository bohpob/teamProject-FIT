package cz.cvut.fit.sp.chipin.base.group.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupCreateGroupRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
}
