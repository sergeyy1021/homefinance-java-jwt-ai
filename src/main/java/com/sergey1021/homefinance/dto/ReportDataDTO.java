package com.sergey1021.homefinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDataDTO {
    private String period;
    private String category;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal balance;
}
