package cz.cvut.fit.sp.chipin.authentication.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    //    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query(
            "UPDATE User a " + "SET a.enabled = TRUE where a.email = ?1"
    )
    int enableUser(String email);

}