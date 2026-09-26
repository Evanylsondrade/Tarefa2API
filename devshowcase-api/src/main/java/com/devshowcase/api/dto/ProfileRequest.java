package com.devshowcase.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação/atualização de Profile.
 * Contém validações obrigatórias de Bean Validation.
 */
public record ProfileRequest(

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    String name,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    String email,

    @Size(max = 500, message = "A bio deve ter no máximo 500 caracteres")
    String bio,

    @Pattern(
        regexp = "^(https?://github\\.com/.+)?$",
        message = "URL do GitHub inválida. Deve iniciar com https://github.com/"
    )
    String githubUrl,

    @Pattern(
        regexp = "^(https?://.*linkedin\\.com/.*)?$",
        message = "URL do LinkedIn inválida"
    )
    String linkedinUrl,

    @Pattern(
        regexp = "^(https?://.+)?$",
        message = "URL do avatar inválida. Deve ser uma URL válida iniciando com http:// ou https://"
    )
    String avatarUrl

) {}
