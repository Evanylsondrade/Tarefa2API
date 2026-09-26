package com.devshowcase.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração OpenAPI 3 / Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevShowcase API")
                        .version("1.0.0")
                        .description("API REST da plataforma DevShowcase — Portfólio colaborativo para desenvolvedores, com gerenciamento de perfis, projetos, tecnologias, avaliações (feedbacks) e upvotes.")
                        .contact(new Contact()
                                .name("DevShowcase Team")
                                .email("contato@devshowcase.com")))
                .servers(List.of(
                        new Server().url("/").description("Servidor da Aplicação")
                ));
    }
}
