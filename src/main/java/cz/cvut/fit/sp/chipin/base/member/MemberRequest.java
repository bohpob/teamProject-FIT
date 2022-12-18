package cz.cvut.fit.sp.chipin.base.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class MemberRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
