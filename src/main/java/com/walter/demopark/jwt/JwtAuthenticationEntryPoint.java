package com.walter.demopark.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Classe JwtAuthenticationEntryPoint que implementa a interface AuthenticationEntryPoint.
 * Essa classe é responsável por iniciar o processo de resposta em caso de uma falha de autenticação JWT,
 * retornando um erro HTTP 401 (Não Autorizado) ao cliente.
 *
 * A anotação @Slf4j é utilizada para habilitar a funcionalidade de logging via o framework Lombok,
 * permitindo o uso da variável 'log' para registrar informações de log.
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Método 'commence' sobrescrito da interface AuthenticationEntryPoint.
     * Esse método é invocado quando uma autenticação falha. Ele registra o erro no log e
     * responde ao cliente com o código de status HTTP 401 (Não Autorizado).
     *
     * @param request         O objeto HttpServletRequest que contém a requisição HTTP.
     * @param response        O objeto HttpServletResponse que contém a resposta HTTP.
     * @param authException   Exceção que contém detalhes sobre a falha de autenticação.
     * @throws IOException    Exceção lançada em caso de erro de I/O.
     * @throws ServletException Exceção lançada em caso de erro de servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Loga a mensagem de erro de autenticação com o status 401
        log.info("Http Status 401 {}", authException.getMessage());

        // Define o cabeçalho de autenticação para o cliente indicando o uso de Bearer token
        response.setHeader("WWW-Authenticate", "Bearer realm='/api/v1/auth'");

        // Envia o erro HTTP 401 (Não Autorizado) como resposta
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

