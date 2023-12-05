package cz.cvut.fit.sp.chipin.base.member.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberReadMemberResponse {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String userRole;
}
