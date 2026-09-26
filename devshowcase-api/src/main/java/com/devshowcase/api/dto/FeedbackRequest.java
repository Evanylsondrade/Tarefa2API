package com.devshowcase.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação de Feedback/Avaliação em um projeto.
 */
public record FeedbackRequest(
    @NotBlank(message = "Nome do autor é obrigatório")
    @Size(max = 100, message = "Nome do autor deve ter no máximo 100 caracteres")
    String authorName,

    @NotBlank(message = "Comentário é obrigatório")
    @Size(max = 2000, message = "Comentário deve ter no máximo 2000 caracteres")
    String comment,

    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    Integer rating
) {}
