package cz.cvut.fit.sp.chipin.base.debt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, DebtKey> {

    Optional<Debt> findByGroupIdAndLenderIdAndBorrowerId(Long groupId, Long lenderId, Long borrowerId);

    ArrayList<Debt> findDebtsByGroupId(Long groupId);
}