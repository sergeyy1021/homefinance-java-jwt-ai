package com.sergey1021.homefinance.dto;

import com.sergey1021.homefinance.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private Long category;
    private String categoryName;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
}
