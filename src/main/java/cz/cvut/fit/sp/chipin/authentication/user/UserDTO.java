package cz.cvut.fit.sp.chipin.authentication.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserDTO {
    @NotNull
    @NotBlank
    private final String name;
}
