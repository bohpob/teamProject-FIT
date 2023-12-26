package cz.cvut.fit.sp.chipin.authentication.user.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserReadUserResponse {
    @NotBlank
    private final String id;
    @NotBlank
    private final String firstName;
    @NotBlank
    private final String lastName;
    @NotBlank
    private final String email;
}
