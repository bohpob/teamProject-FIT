package cz.cvut.fit.sp.chipin.base.usergroup;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GroupCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
}
