package cz.cvut.fit.sp.chipin.authentication.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserGroupResponse {
    @NotNull
    private final Long id;
    @NotBlank
    private final String name;
    @NotNull
    private final Float balance;
}
