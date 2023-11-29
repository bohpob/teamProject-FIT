package cz.cvut.fit.sp.chipin.base.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberDTO {
    @NotNull
    private Long groupId;
    @NotBlank
    private String role;
    @NotNull
    private Float paid;
    @NotNull
    private Float spent;
    @NotNull
    private Float balance;
}
