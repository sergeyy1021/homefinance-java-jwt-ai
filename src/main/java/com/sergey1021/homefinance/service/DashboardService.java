package com.sergey1021.homefinance.service;

import com.sergey1021.homefinance.dto.SummaryDTO;
import com.sergey1021.homefinance.enums.TransactionType;
import com.sergey1021.homefinance.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Slf4j
@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public SummaryDTO getSummary() {

        BigDecimal totalIncome = transactionRepository.sumByType(TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByType(TransactionType.EXPENSE);

        // Обработка случая, когда нет транзакций
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new SummaryDTO(totalIncome, totalExpense, balance);
    }
}
