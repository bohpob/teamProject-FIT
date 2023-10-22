package cz.cvut.fit.sp.chipin.base.usergroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserGroupCreateRequest {
    @NotNull
    private Long userAccountId;
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
}