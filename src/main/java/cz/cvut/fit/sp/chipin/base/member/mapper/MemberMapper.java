package cz.cvut.fit.sp.chipin.base.member.mapper;

import cz.cvut.fit.sp.chipin.base.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source = "userAccount.userEntity.firstName", target = "name")
    @Mapping(source = "role", target = "userRole")
    MemberReadMemberResponse entityToReadMemberResponse(Member member);

    List <MemberReadMemberResponse> entityToReadMemberResponse(List<Member> members);
}
