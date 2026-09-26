package com.devshowcase.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação de Technology.
 */
public record TechnologyRequest(

    @NotBlank(message = "O nome da tecnologia é obrigatório")
    @Size(min = 1, max = 100, message = "O nome deve ter no máximo 100 caracteres")
    String name,

    @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres")
    String category,

    @Pattern(
        regexp = "^(https?://.+)?$",
        message = "URL do ícone inválida. Deve ser uma URL válida iniciando com http:// ou https://"
    )
    String iconUrl

) {}
