package cz.cvut.fit.sp.chipin.base.member.mapper;

import cz.cvut.fit.sp.chipin.base.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source = "id.userId", target = "id")
    @Mapping(source = "user.firstName", target = "name")
    MemberReadMemberResponse entityToReadMemberResponse(Member member);
}
