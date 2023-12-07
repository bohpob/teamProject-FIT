package cz.cvut.fit.sp.chipin.base.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionsByUserGroupId(Long groupId);

    List<Transaction> findTransactionByUserGroupIdAndCategoryIn(Long groupId, List<Category> categories);
}
