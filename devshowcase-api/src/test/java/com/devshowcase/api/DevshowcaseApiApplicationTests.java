package com.devshowcase.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Teste básico que verifica se o contexto Spring Boot carrega corretamente.
 */
@SpringBootTest
@ActiveProfiles("test")
class DevshowcaseApiApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que o contexto da aplicação inicializa sem erros
    }
}
