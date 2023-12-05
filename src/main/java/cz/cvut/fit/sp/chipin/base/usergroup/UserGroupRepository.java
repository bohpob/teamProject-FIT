package cz.cvut.fit.sp.chipin.base.usergroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findUserGroupByHexCode(String hexCode);
}
