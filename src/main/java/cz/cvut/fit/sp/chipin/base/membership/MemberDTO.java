package cz.cvut.fit.sp.chipin.base.membership;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class MemberDTO {
    @NotNull
    private Long group_id;
    @NotBlank
    private String role;
    @NotNull
    private Float paid;
    @NotNull
    private Float spent;
    @NotNull
    private Float balance;
}
