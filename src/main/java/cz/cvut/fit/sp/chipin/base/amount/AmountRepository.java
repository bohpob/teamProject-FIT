package cz.cvut.fit.sp.chipin.base.amount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountRepository extends JpaRepository<Amount, AmountKey> {
    void deleteAmountsByTransactionId(Long transaction_id);
}
