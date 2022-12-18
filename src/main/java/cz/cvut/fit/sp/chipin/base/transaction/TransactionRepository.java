package cz.cvut.fit.sp.chipin.base.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    ArrayList<Transaction> getTransactionsByGroupId(Long groupId);
}
