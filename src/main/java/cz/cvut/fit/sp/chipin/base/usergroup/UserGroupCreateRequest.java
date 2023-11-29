package cz.cvut.fit.sp.chipin.base.usergroup;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserGroupCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
}