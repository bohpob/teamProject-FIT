package cz.cvut.fit.sp.chipin.authentication.useraccount.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserReadUserResponse {
    @NotBlank
    private final String name;
    @NotBlank
    private final String email;
}
