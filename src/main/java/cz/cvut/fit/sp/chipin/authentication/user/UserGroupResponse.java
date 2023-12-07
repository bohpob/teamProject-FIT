package cz.cvut.fit.sp.chipin.authentication.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserGroupResponse {
    @NotNull
    private final String id;
    @NotBlank
    private final String name;
    @NotNull
    private final Float balance;
}
