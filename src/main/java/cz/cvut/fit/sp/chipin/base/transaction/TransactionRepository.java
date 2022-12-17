package cz.cvut.fit.sp.chipin.base.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT t.id FROM Transaction t WHERE t.payer.group.id=?1")
    ArrayList<Long> getTransactionIdsByGroupId(Long groupId);
}
