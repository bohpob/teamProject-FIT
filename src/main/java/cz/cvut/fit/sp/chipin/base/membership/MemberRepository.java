package cz.cvut.fit.sp.chipin.base.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberKey> {
    Optional<Member> findByUserIdAndGroupId(Long userId, Long groupId);
}