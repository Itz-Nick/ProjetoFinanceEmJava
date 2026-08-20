package com.nikofinance.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String name;

    @Size(max = 50, message = "Ícone deve ter no máximo 50 caracteres")
    private String icon;

    @Size(max = 7, message = "Cor deve ter no máximo 7 caracteres (formato hex)")
    private String color;
}