package com.sergey1021.homefinance.controller;

import com.sergey1021.homefinance.dto.TransactionDTO;
import com.sergey1021.homefinance.enums.TransactionType;
import com.sergey1021.homefinance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public Map<String, Object> getFilteredTransactions(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        TransactionType transactionType = null;
        if (type != null && !type.equals("ALL")) {
            transactionType = TransactionType.valueOf(type);
        }

        return transactionService.getFilteredTransactions(
                transactionType, categoryId, startDate, endDate);
    }

    @GetMapping("/recent")
    public List<TransactionDTO> getRecentTransactions() {
        return transactionService.getRecentTransactions();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @PutMapping("/{id}")
    public TransactionDTO updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}