package cz.cvut.fit.sp.chipin.authentication.user;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserDTO {
    @NotBlank
    private final String name;
}
