package cz.cvut.fit.sp.chipin.base.member;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> readMember(Long userAccountId, Long groupId) throws Exception {
        return memberRepository.findByUserAccountIdAndUserGroupId(userAccountId, groupId);
    }

    public void save(Member member) throws Exception {
        memberRepository.save(member);
    }

    public ArrayList<Member> readMembers(Long groupId) throws Exception {
        return memberRepository.findMembersByUserGroupId(groupId);
    }
}
