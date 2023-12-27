package cz.cvut.fit.sp.chipin.authentication.user.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateTransactionResponse {
    @NotBlank
    private String id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
