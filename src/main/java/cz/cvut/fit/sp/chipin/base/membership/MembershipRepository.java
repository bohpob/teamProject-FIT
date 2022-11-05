package cz.cvut.fit.sp.chipin.base.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, MembershipKey> {
    Optional<Membership> findByUserIdAndGroupId(Long userId, Long groupId);
}
