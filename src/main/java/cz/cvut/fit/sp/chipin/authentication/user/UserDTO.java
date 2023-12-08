package cz.cvut.fit.sp.chipin.authentication.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {
    @NotBlank
    private final String name;
}
