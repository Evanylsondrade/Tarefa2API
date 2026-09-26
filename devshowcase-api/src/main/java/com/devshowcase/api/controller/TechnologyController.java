package com.devshowcase.api.controller;

import com.devshowcase.api.dto.TechnologyRequest;
import com.devshowcase.api.dto.TechnologyResponse;
import com.devshowcase.api.service.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de Tecnologia (Technology).
 */
@RestController
@RequestMapping("/api/technologies")
@Tag(name = "Tecnologias", description = "Endpoints para gerenciamento de linguagens, frameworks e ferramentas")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    /**
     * POST /api/technologies
     * Cadastra uma nova tecnologia.
     */
    @Operation(summary = "Cadastrar tecnologia", description = "Cadastra uma nova tecnologia como Java, Spring Boot, PostgreSQL.")
    @PostMapping
    public ResponseEntity<TechnologyResponse> create(@Valid @RequestBody TechnologyRequest request) {
        TechnologyResponse response = technologyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/technologies
     * Lista todas as tecnologias cadastradas.
     */
    @Operation(summary = "Listar todas as tecnologias", description = "Retorna a lista completa das tecnologias cadastradas.")
    @GetMapping
    public ResponseEntity<List<TechnologyResponse>> findAll() {
        List<TechnologyResponse> technologies = technologyService.findAll();
        return ResponseEntity.ok(technologies);
    }
}
