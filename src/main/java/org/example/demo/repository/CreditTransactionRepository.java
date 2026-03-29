package org.example.demo.repository;

import org.example.demo.entity.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

    List<CreditTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Get recent transactions (for dashboard)
    List<CreditTransaction> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
