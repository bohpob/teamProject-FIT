package cz.cvut.fit.sp.chipin.base.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MemberRequest {
    @NotNull
    private String id;
    @NotBlank
    private String name;
}
