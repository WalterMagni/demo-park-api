package com.walter.demopark;

import com.walter.demopark.jwt.JwtToken;
import com.walter.demopark.web.dto.usuario.UsuarioLoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    /**
     * Obtém um cabeçalho de autorização (Authorization header) com um token JWT usando as credenciais fornecidas.
     *
     * Esse método realiza a autenticação em uma API usando um cliente de teste (WebTestClient) e as credenciais de
     * nome de usuário e senha fornecidas. Ele envia uma requisição POST para a URI "/api/v1/auth", espera um status
     * de resposta 200 OK, extrai o token JWT da resposta e retorna um consumidor de HttpHeaders que pode ser usado
     * para adicionar o cabeçalho de autorização com o token nas requisições subsequentes.
     *
     * @param testClient O WebTestClient usado para realizar a requisição de autenticação.
     * @param username O nome de usuário a ser usado para autenticação.
     * @param password A senha a ser usada para autenticação.
     * @return Um Consumer<HttpHeaders> que adiciona o cabeçalho de autorização com o token JWT no formato "Bearer {token}".
     */

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient testClient, String username, String password) {
        // Realiza uma requisição POST para "/api/v1/auth" com as credenciais fornecidas
        String token = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto(username, password)) // Envia o corpo da requisição com os dados do login
                .exchange() // Executa a requisição
                .expectStatus().isOk() // Verifica se o status da resposta é 200 OK
                .expectBody(JwtToken.class) // Verifica se o corpo da resposta contém um objeto JwtToken
                .returnResult().getResponseBody().getToken(); // Extrai o token JWT da resposta

        // Retorna um Consumer<HttpHeaders> que adiciona o cabeçalho Authorization com o token JWT
        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }
}
