package com.sergey1021.homefinance.service;

import com.sergey1021.homefinance.dto.ReportDataDTO;
import com.sergey1021.homefinance.enums.ReportType;
import com.sergey1021.homefinance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<ReportDataDTO> generateReport(
            ReportType reportType,
            LocalDate startDate,
            LocalDate endDate) {

        if (reportType == ReportType.MONTHLY) {
            return generateMonthlyReport(startDate, endDate);
        } else {
            return generateCategoryReport(startDate, endDate);
        }
    }

    private List<ReportDataDTO> generateMonthlyReport(LocalDate startDate, LocalDate endDate) {
        List<Object[]> reportData = transactionRepository.getMonthlyReport(startDate, endDate);
        List<ReportDataDTO> result = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Object[] row : reportData) {
            Integer year = (Integer) row[0];
            Integer month = (Integer) row[1];
            BigDecimal income = (BigDecimal) row[2];
            BigDecimal expense = (BigDecimal) row[3];

            String period = String.format("%04d-%02d", year, month);

            BigDecimal balance = income.subtract(expense);

            ReportDataDTO dto = new ReportDataDTO();
            dto.setPeriod(period);
            dto.setIncome(income);
            dto.setExpense(expense);
            dto.setBalance(balance);

            result.add(dto);
        }

        return result;
    }

    private List<ReportDataDTO> generateCategoryReport(LocalDate startDate, LocalDate endDate) {
        List<Object[]> reportData = transactionRepository.getCategoryReport(startDate, endDate);
        List<ReportDataDTO> result = new ArrayList<>();

        for (Object[] row : reportData) {
            Long categoryId = (Long) row[0];
            String categoryName = (String) row[1];
            BigDecimal income = (BigDecimal) row[2];
            BigDecimal expense = (BigDecimal) row[3];

            BigDecimal balance = income.subtract(expense);

            ReportDataDTO dto = new ReportDataDTO();
            dto.setCategory(categoryName);
            dto.setIncome(income);
            dto.setExpense(expense);
            dto.setBalance(balance);

            result.add(dto);
        }

        return result;
    }
}