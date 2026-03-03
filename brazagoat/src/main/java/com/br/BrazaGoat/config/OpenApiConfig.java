package com.br.BrazaGoat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI brazaGoatOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BrazaGoat API")
                        .description("Sistema de gerenciamento de partidas de futebol de salão. " +
                                "Gerencie jogadores, sorteie equipes, registre partidas, gols, assistências e substituições.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("BrazaGoat")
                                .email("contato@brazagoat.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente local")
                ));
    }
}
