package cz.cvut.fit.sp.chipin.base.member;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> getMember(Long userId, Long groupId ) throws Exception {
        return memberRepository.findByUserIdAndGroupId(userId, groupId);
    }

    public void save(Member member) throws Exception {
        memberRepository.save(member);
    }

    public ArrayList<Member> getMembersByGroupId(Long groupId) throws Exception {
        return memberRepository.findMembersByGroupId(groupId);
    }
}
