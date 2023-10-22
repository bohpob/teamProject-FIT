package cz.cvut.fit.sp.chipin.authentication.useraccount;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountDTO {
    @NotBlank
    private final String name;
}
