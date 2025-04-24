package com.sergey1021.homefinance.service;

import com.sergey1021.homefinance.dto.CategoryDTO;
import com.sergey1021.homefinance.dto.TransactionDTO;
import com.sergey1021.homefinance.entity.Category;
import com.sergey1021.homefinance.entity.Transaction;
import com.sergey1021.homefinance.enums.TransactionType;
import com.sergey1021.homefinance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getRecentTransactions() {
        return transactionRepository.findTop10ByOrderByDateDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getFilteredTransactions(
            TransactionType type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate) {

        List<Transaction> transactions;

        // Устанавливаем значения дат по умолчанию, если они не указаны
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        if (type != null && categoryId != null) {
            Category category = categoryService.findEntityById(categoryId);
            transactions = transactionRepository.findByTypeAndCategoryAndDateBetweenOrderByDateDesc(
                    type, category, start, end);
        } else if (type != null) {
            transactions = transactionRepository.findByTypeAndDateBetweenOrderByDateDesc(
                    type, start, end);
        } else if (categoryId != null) {
            Category category = categoryService.findEntityById(categoryId);
            transactions = transactionRepository.findByCategoryAndDateBetweenOrderByDateDesc(
                    category, start, end);
        } else {
            transactions = transactionRepository.findByDateBetweenOrderByDateDesc(
                    start, end);
        }

        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<CategoryDTO> categories = categoryService.getAllCategories();

        Map<String, Object> result = new HashMap<>();
        result.put("transactions", transactionDTOs);
        result.put("categories", categories);

        return result;
    }

    public TransactionDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        updateTransactionFields(transaction, transactionDTO);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }

    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));

        updateTransactionFields(transaction, transactionDTO);

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToDTO(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    private void updateTransactionFields(Transaction transaction, TransactionDTO dto) {
        transaction.setType(dto.getType());

        Category category = categoryService.findEntityById(dto.getCategory());
        transaction.setCategory(category);

        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setDescription(dto.getDescription());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setType(transaction.getType());
        dto.setCategory(transaction.getCategory().getId());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setDescription(transaction.getDescription());
        return dto;
    }
}
