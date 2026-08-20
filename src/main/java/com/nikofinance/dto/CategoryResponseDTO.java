package com.nikofinance.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String icon;
    private String color;
    private boolean isDefault;
    private long transactionCount;
}