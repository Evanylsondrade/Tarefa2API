package com.devshowcase.api.controller;

import com.devshowcase.api.dto.ProfileRequest;
import com.devshowcase.api.dto.ProfileResponse;
import com.devshowcase.api.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operações de Perfil (Profile).
 */
@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Perfis", description = "Endpoints para gerenciamento de perfis de desenvolvedores")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * POST /api/profiles
     * Cadastra um novo perfil de desenvolvedor.
     */
    @Operation(summary = "Cadastrar perfil", description = "Cadastra um novo perfil de desenvolvedor com dados como nome, email e bio.")
    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/profiles/{id}
     * Busca um perfil pelo ID.
     */
    @Operation(summary = "Buscar perfil por ID", description = "Retorna os detalhes do perfil correspondente ao ID informado.")
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> findById(@PathVariable Long id) {
        ProfileResponse response = profileService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/profiles/{id}
     * Exclui um perfil pelo ID.
     */
    @Operation(summary = "Excluir perfil por ID", description = "Exclui o perfil e seus projetos associados.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        profileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
