package com.walter.demopark.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da documentação da API utilizando SpringDoc e OpenAPI.
 *
 * Esta classe configura a documentação da API REST para o projeto usando a especificação OpenAPI 3.
 * Através da anotação @Configuration, esta classe é registrada como uma configuração do Spring.
 * A documentação é gerada automaticamente e inclui informações gerais sobre a API, como o título, descrição,
 * versão, licença e informações de contato, além de configurar o esquema de segurança baseado em tokens JWT.
 */
@Configuration
public class SpringDocOpenApiConfig {

    /**
     * Cria um bean do tipo OpenAPI que configura a documentação da API.
     *
     * Esse método constrói um objeto OpenAPI que define os componentes da documentação, incluindo esquemas de
     * segurança e as informações da API (como título, descrição, versão, licença e contato).
     *
     * @return Um objeto OpenAPI configurado com os detalhes da API e os esquemas de segurança.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // Adiciona o esquema de segurança à documentação da API
                .components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(
                        new Info()
                                .title("REST API - Spring Park")   // Define o título da API
                                .description("API REST para gestão de estacionamento")  // Define a descrição da API
                                .version("1.0.0")  // Define a versão da API
                                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))  // Define a licença da API
                                .contact(new Contact().name("Walter Magni").email("waltermagni@terra.com.br"))  // Define as informações de contato do responsável pela API
                );
    }

    /**
     * Configura o esquema de segurança para autenticação JWT.
     *
     * Este método define o esquema de segurança como HTTP com o tipo "bearer", que será utilizado para
     * autenticar as requisições via token JWT (JSON Web Token). O token será inserido no cabeçalho da requisição.
     *
     * @return Um objeto SecurityScheme configurado para autenticação via JWT.
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .description("Insira um bearer token para autenticar")  // Descrição do esquema de segurança
                .type(SecurityScheme.Type.HTTP)  // Tipo de esquema de segurança: HTTP
                .in(SecurityScheme.In.HEADER)  // O token será passado no cabeçalho da requisição
                .scheme("bearer")  // Esquema de autenticação: "bearer"
                .bearerFormat("JWT")  // Formato do token: JWT
                .name("security");  // Nome do esquema de segurança
    }
}
