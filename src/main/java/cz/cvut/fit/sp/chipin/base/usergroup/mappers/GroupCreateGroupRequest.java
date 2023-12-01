package cz.cvut.fit.sp.chipin.base.usergroup.mappers;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GroupCreateGroupRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
}
