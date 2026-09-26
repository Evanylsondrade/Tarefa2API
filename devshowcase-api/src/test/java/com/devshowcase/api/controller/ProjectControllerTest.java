package com.devshowcase.api.controller;

import com.devshowcase.api.dto.FeedbackRequest;
import com.devshowcase.api.dto.ProfileRequest;
import com.devshowcase.api.dto.ProjectRequest;
import com.devshowcase.api.dto.TechnologyRequest;
import com.devshowcase.api.repository.ProfileRepository;
import com.devshowcase.api.repository.ProjectRepository;
import com.devshowcase.api.repository.TechnologyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Long profileId;
    private Long technologyId;

    @BeforeEach
    void setUp() throws Exception {
        projectRepository.deleteAll();
        profileRepository.deleteAll();
        technologyRepository.deleteAll();

        // Cadastra um perfil para os testes
        ProfileRequest profileRequest = new ProfileRequest(
                "Carlos Tester",
                "carlos.tester@example.com",
                "Dev Java",
                "https://github.com/carlostester",
                "https://linkedin.com/in/carlostester",
                "https://avatar.png"
        );
        MvcResult profileResult = mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String profileResponseJson = profileResult.getResponse().getContentAsString();
        profileId = objectMapper.readTree(profileResponseJson).get("id").asLong();

        // Cadastra uma tecnologia para os testes
        TechnologyRequest techRequest = new TechnologyRequest("Java", "LANGUAGE", "https://java-icon.png");
        MvcResult techResult = mockMvc.perform(post("/api/technologies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(techRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String techResponseJson = techResult.getResponse().getContentAsString();
        technologyId = objectMapper.readTree(techResponseJson).get("id").asLong();
    }

    @Test
    @DisplayName("Deve cadastrar um projeto com sucesso")
    void deveCriarProjetoComSucesso() throws Exception {
        ProjectRequest request = new ProjectRequest(
                "DevShowcase API",
                "API de Portfólio",
                "https://github.com/carlostester/devshowcase-api",
                "https://devshowcase.app",
                "https://cover.png",
                profileId,
                List.of(technologyId)
        );

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("DevShowcase API"))
                .andExpect(jsonPath("$.upvotes").value(0))
                .andExpect(jsonPath("$.averageRating").value(0.0));
    }

    @Test
    @DisplayName("Deve cadastrar um feedback e atualizar a nota média do projeto")
    void deveCadastrarFeedbackERecalcularNotaMedia() throws Exception {
        // Criar projeto
        ProjectRequest projectRequest = new ProjectRequest(
                "Projeto Feedback Test",
                "Descrição",
                "https://github.com/repo",
                null,
                null,
                profileId,
                List.of(technologyId)
        );

        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long projectId = objectMapper.readTree(projectResult.getResponse().getContentAsString()).get("id").asLong();

        // Adicionar primeiro feedback nota 4
        FeedbackRequest fb1 = new FeedbackRequest("Reviewer 1", "Excelente projeto!", 4);
        mockMvc.perform(post("/api/projects/{id}/feedbacks", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fb1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(4));

        // Adicionar segundo feedback nota 5
        FeedbackRequest fb2 = new FeedbackRequest("Reviewer 2", "Ótima arquitetura!", 5);
        mockMvc.perform(post("/api/projects/{id}/feedbacks", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fb2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));

        // Buscar projeto e validar nota média (4 + 5) / 2 = 4.5
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].averageRating").value(4.5));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar cadastrar feedback com nota fora da faixa 1-5")
    void deveRetornar400ParaFeedbackNotaInvalida() throws Exception {
        // Criar projeto
        ProjectRequest projectRequest = new ProjectRequest(
                "Projeto Teste Erro",
                "Descrição",
                "https://github.com/repo",
                null,
                null,
                profileId,
                null
        );

        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long projectId = objectMapper.readTree(projectResult.getResponse().getContentAsString()).get("id").asLong();

        // Feedback com nota 10 (inválida)
        FeedbackRequest fbInvalido = new FeedbackRequest("Reviewer", "Muito ruim", 10);
        mockMvc.perform(post("/api/projects/{id}/feedbacks", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fbInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve incrementar curtidas/upvotes com sucesso ao chamar PUT /api/projects/{id}/upvote")
    void deveIncrementarUpvotes() throws Exception {
        // Criar projeto
        ProjectRequest projectRequest = new ProjectRequest(
                "Projeto Upvote Test",
                "Descrição",
                "https://github.com/repo",
                null,
                null,
                profileId,
                null
        );

        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long projectId = objectMapper.readTree(projectResult.getResponse().getContentAsString()).get("id").asLong();

        // Chamar upvote 1ª vez
        mockMvc.perform(put("/api/projects/{id}/upvote", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.upvotes").value(1));

        // Chamar upvote 2ª vez
        mockMvc.perform(put("/api/projects/{id}/upvote", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.upvotes").value(2));
    }

    @Test
    @DisplayName("Deve buscar projetos com filtro de tecnologia e paginação")
    void deveBuscarProjetosComFiltroEPaginacao() throws Exception {
        // Criar projeto associado à tecnologia Java
        ProjectRequest projectRequest = new ProjectRequest(
                "Projeto Java",
                "Projeto em Java",
                "https://github.com/repo",
                null,
                null,
                profileId,
                List.of(technologyId)
        );

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated());

        // Filtrar por ID da tecnologia
        mockMvc.perform(get("/api/projects")
                        .param("technologyId", technologyId.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Projeto Java"))
                .andExpect(jsonPath("$.totalElements").value(1));

        // Filtrar por nome da tecnologia "Java"
        mockMvc.perform(get("/api/projects")
                        .param("technology", "Java")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Projeto Java"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao dar upvote em projeto inexistente")
    void deveRetornar404UpvoteProjetoInexistente() throws Exception {
        mockMvc.perform(put("/api/projects/{id}/upvote", 99999L))
                .andExpect(status().isNotFound());
    }
}
