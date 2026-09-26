package com.devshowcase.api.controller;

import com.devshowcase.api.dto.ProfileRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve cadastrar um novo perfil com sucesso")
    void deveCriarPerfilComSucesso() throws Exception {
        ProfileRequest request = new ProfileRequest(
                "Maria Oliveira",
                "maria.oliveira@example.com",
                "Desenvolvedora Backend Java",
                "https://github.com/mariaoliveira",
                "https://linkedin.com/in/mariaoliveira",
                "https://example.com/avatar.png"
        );

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Maria Oliveira"))
                .andExpect(jsonPath("$.email").value("maria.oliveira@example.com"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar cadastrar perfil com dados inválidos")
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        ProfileRequest requestInvalido = new ProfileRequest(
                "", // Nome em branco
                "email-invalido", // Formato inválido
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar ID inexistente")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        mockMvc.perform(get("/api/profiles/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao tentar deletar ID inexistente")
    void deveRetornar404AoDeletarIdInexistente() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/profiles/{id}", 99999L))
                .andExpect(status().isNotFound());
    }
}
