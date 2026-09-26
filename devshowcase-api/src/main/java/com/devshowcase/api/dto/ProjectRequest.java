package com.devshowcase.api.dto;

import jakarta.validation.constraints.*;

import java.util.List;

/**
 * DTO de entrada para criação de Project.
 */
public record ProjectRequest(

    @NotBlank(message = "O título do projeto é obrigatório")
    @Size(min = 2, max = 150, message = "O título deve ter entre 2 e 150 caracteres")
    String title,

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    String description,

    @Pattern(
        regexp = "^(https?://.+)?$",
        message = "URL do repositório inválida. Deve ser uma URL válida iniciando com http:// ou https://"
    )
    String repositoryUrl,

    @Pattern(
        regexp = "^(https?://.+)?$",
        message = "URL da demo inválida. Deve ser uma URL válida iniciando com http:// ou https://"
    )
    String demoUrl,

    @Pattern(
        regexp = "^(https?://.+)?$",
        message = "URL da thumbnail inválida. Deve ser uma URL válida iniciando com http:// ou https://"
    )
    String thumbnailUrl,

    @NotNull(message = "O ID do perfil (profileId) é obrigatório")
    @Positive(message = "O profileId deve ser um número positivo")
    Long profileId,

    /** IDs das tecnologias associadas ao projeto */
    List<Long> technologyIds

) {}
