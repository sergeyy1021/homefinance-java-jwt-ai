package com.sergey1021.homefinance.repository;

import com.sergey1021.homefinance.entity.Category;
import com.sergey1021.homefinance.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByType(TransactionType type);
}
