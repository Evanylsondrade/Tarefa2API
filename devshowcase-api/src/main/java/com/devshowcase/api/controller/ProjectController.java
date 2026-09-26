package com.devshowcase.api.controller;

import com.devshowcase.api.dto.FeedbackRequest;
import com.devshowcase.api.dto.FeedbackResponse;
import com.devshowcase.api.dto.ProjectRequest;
import com.devshowcase.api.dto.ProjectResponse;
import com.devshowcase.api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operações de Projetos (Project).
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos de desenvolvedores, curtidas e avaliações")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * POST /api/projects
     * Cadastra um novo projeto associado a um perfil.
     */
    @Operation(summary = "Cadastrar projeto", description = "Cadastra um novo projeto vinculado a um perfil existente e a tecnologias.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Projeto cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Perfil ou Tecnologia não encontrada")
    })
    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/projects
     * Lista projetos com filtragem opcional por tecnologia e suporte a paginação.
     */
    @Operation(summary = "Buscar projetos com filtro por tecnologia e paginação", 
               description = "Retorna uma página de projetos. É possível filtrar por ID da tecnologia (technologyId) ou por nome (technology).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista paginada de projetos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> findAll(
            @RequestParam(required = false) Long technologyId,
            @RequestParam(required = false) String technology,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProjectResponse> projects = projectService.findAllPaginated(technologyId, technology, pageable);
        return ResponseEntity.ok(projects);
    }

    /**
     * POST /api/projects/{id}/feedbacks
     * Cadastra uma nota de 1 a 5 e comentário, recalculando a média do projeto.
     */
    @Operation(summary = "Cadastrar feedback para projeto", 
               description = "Cadastra uma avaliação (nota 1 a 5 e comentário) em um projeto. A nota média do projeto é recalculada automaticamente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Feedback cadastrado e nota média atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados do feedback inválidos (ex: nota fora do intervalo 1 a 5)"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PostMapping("/{id}/feedbacks")
    public ResponseEntity<FeedbackResponse> addFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequest request) {
        FeedbackResponse response = projectService.addFeedback(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/projects/{id}/upvote
     * Atualiza as curtidas/estrelas incrementando-as.
     */
    @Operation(summary = "Incrementar curtidas/upvotes do projeto", 
               description = "Incrementa o contador de upvotes (estrelas/curtidas) do projeto especificado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Upvote registrado com sucesso e total atualizado"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PutMapping("/{id}/upvote")
    public ResponseEntity<ProjectResponse> upvote(@PathVariable Long id) {
        ProjectResponse response = projectService.upvote(id);
        return ResponseEntity.ok(response);
    }
}
