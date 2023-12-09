package cz.cvut.fit.sp.chipin.base.group.mapper;


import cz.cvut.fit.sp.chipin.base.member.mapper.MemberReadMemberResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupReadGroupMembersResponse {

    @NotBlank
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private List<MemberReadMemberResponse> members;
}
