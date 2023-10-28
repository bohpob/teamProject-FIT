package cz.cvut.fit.sp.chipin.base.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberKey> {
    Optional<Member> findByUserAccountIdAndUserGroupId(String userAccountId, Long groupId);

    ArrayList<Member> findMembersByUserGroupId(Long groupId);

}
