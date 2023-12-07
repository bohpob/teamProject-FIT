package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.base.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionsByUserGroupId(Long groupId);

    List<Transaction> findTransactionByUserGroupIdAndCategoryIn(Long groupId, List<Category> categories);

    List<Transaction> findTransactionsByUserGroupIdAndDateBetween(Long groupId, String dateFrom, String dateTo);

//    List<Transaction> findMemberTransactionsByUserGroupId(Long groupId, List<Member> members);

}
