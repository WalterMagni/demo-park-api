package com.walter.demopark;

import com.walter.demopark.jwt.JwtToken;
import com.walter.demopark.web.dto.usuario.UsuarioLoginDto;
import com.walter.demopark.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/database/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/database/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AutentiticaoIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void autenticarComUsernameEPasswordValidos_RetornarTokenComStatus200() {

        JwtToken responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto("ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }


    @Test
    public void autenticarComUsernameEPasswordInvalidos_RetornarErrorMessageStatus400() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto("jorge@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto("ana@email.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void autenticarComUsernameInvalido_RetornarErrorMessageStatus422() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto("@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void autenticarComSenhaInvalida_RetornarErrorMessageStatus422() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto("ana@gmail.com", "123"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto("ana@email.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

}
