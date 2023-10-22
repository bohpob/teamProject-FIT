package cz.cvut.fit.sp.chipin.authentication.useraccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    //    Optional<UserAccount> findUserAccountByUserAccountname(String userAccountname);
    Optional<UserAccount> findUserAccountByEmail(String email);

    @Transactional
    @Modifying
    @Query(
            "UPDATE UserAccount a " + "SET a.enabled = TRUE where a.email = ?1"
    )
    int enableUserAccount(String email);

}