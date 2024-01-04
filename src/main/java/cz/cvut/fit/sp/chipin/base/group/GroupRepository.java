package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findGroupByHexCode(String hexCode);

    @Query("SELECT m.user FROM Member m WHERE m.group.id = :groupId")
    List<User> findUsersByGroupId(@Param("groupId") Long groupId);
}
