package com.sergey1021.homefinance.repository;

import com.sergey1021.homefinance.entity.Category;
import com.sergey1021.homefinance.entity.Transaction;
import com.sergey1021.homefinance.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = ?1")
    BigDecimal sumByType(TransactionType type);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = ?1 AND t.date BETWEEN ?2 AND ?3")
    BigDecimal sumByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByTypeAndDateBetweenOrderByDateDesc(
            TransactionType type, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByDateBetweenOrderByDateDesc(
            LocalDate startDate, LocalDate endDate);

    List<Transaction> findByTypeAndCategoryAndDateBetweenOrderByDateDesc(
            TransactionType type, Category category, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByCategoryAndDateBetweenOrderByDateDesc(
            Category category, LocalDate startDate, LocalDate endDate);

    List<Transaction> findTop10ByOrderByDateDesc();

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByTypeOrderByDateDesc(TransactionType type);

    @Query("SELECT t.category.id as categoryId, t.category.name as categoryName, " +
            "SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as expense " +
            "FROM Transaction t WHERE t.date BETWEEN ?1 AND ?2 GROUP BY t.category.id, t.category.name")
    List<Object[]> getCategoryReport(LocalDate startDate, LocalDate endDate);

    @Query("SELECT YEAR(t.date) as year, MONTH(t.date) as month, " +
            "SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as expense " +
            "FROM Transaction t WHERE t.date BETWEEN ?1 AND ?2 " +
            "GROUP BY YEAR(t.date), MONTH(t.date) " +
            "ORDER BY YEAR(t.date), MONTH(t.date)")
    List<Object[]> getMonthlyReport(LocalDate startDate, LocalDate endDate);
}
