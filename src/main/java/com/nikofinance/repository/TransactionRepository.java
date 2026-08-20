package com.nikofinance.repository;

import com.nikofinance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTransactionDateBetweenOrderByTransactionDateDesc(LocalDate startDate, LocalDate endDate);

    List<Transaction> findByTypeAndTransactionDateBetweenOrderByTransactionDateDesc(
            Transaction.TransactionType type, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByCategoryIdOrderByTransactionDateDesc(Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.transactionDate BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumAmountByTypeAndDateRange(
            @Param("type") Transaction.TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type")
    Optional<BigDecimal> sumAmountByType(@Param("type") Transaction.TransactionType type);

    long countByType(Transaction.TransactionType type);

    List<Transaction> findByCategoryIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long categoryId, LocalDate startDate, LocalDate endDate);

    List<Transaction> findAllByOrderByTransactionDateDesc();

    @Query(value = """
        SELECT c.id, c.name, c.icon, c.color, SUM(t.amount), COUNT(t)
        FROM transactions t
        JOIN categories c ON t.category_id = c.id
        WHERE t.transaction_date BETWEEN :startDate AND :endDate
        GROUP BY c.id, c.name, c.icon, c.color
        ORDER BY SUM(t.amount) DESC
        """, nativeQuery = true)
    List<Object[]> getCategorySummary(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}