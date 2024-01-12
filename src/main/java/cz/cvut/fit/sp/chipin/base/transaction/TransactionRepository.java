package cz.cvut.fit.sp.chipin.base.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionsByGroupId(Long groupId);
    Page<Transaction> findTransactionByGroupIdAndCategoryIn(Long groupId, List<Category> categories, Pageable pageable);
    Page<Transaction> findTransactionsByGroupId(Long groupId, Pageable pageable);
//    List<Transaction> findTransactionsByGroupIdAndDateBetween(Long groupId, String dateFrom, String dateTo);

//    List<Transaction> findMemberTransactionsByUserGroupId(Long groupId, List<Member> members);
}
