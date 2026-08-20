package com.nikofinance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private Long id;
    private String type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}