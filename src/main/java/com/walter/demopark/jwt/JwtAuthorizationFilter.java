package com.walter.demopark.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT responsável por interceptar requisições HTTP e verificar se há um token JWT válido no cabeçalho da requisição.
 * Caso o token seja válido, a autenticação do usuário será realizada automaticamente.
 */

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {


    // Injeção de dependência do serviço que carrega os detalhes do usuário (utilizado para autenticação).
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    /**
     * Método responsável por filtrar e processar a autenticação em cada requisição recebida.
     * O método verifica o cabeçalho "Authorization" em busca de um token JWT. Se o token for válido,
     * o método realiza a autenticação do usuário. Caso contrário, o fluxo continua sem autenticação.
     *
     * @param request   A requisição HTTP.
     * @param response  A resposta HTTP.
     * @param filterChain A cadeia de filtros a ser aplicada após a verificação.
     * @throws ServletException Em caso de erro de processamento de servlet.
     * @throws IOException Em caso de erro de IO.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Obtém o token JWT do cabeçalho "Authorization".
        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);

        // Verifica se o token está presente e se começa com o prefixo "Bearer ".
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.info("Jwt Token está nulo, vazio ou não inicia com 'Bearer '");
            // Se o token for inválido ou não estiver presente, passa o controle para o próximo filtro.
            filterChain.doFilter(request, response);
            return;
        }

        // Verifica se o token JWT é válido.
        if (!JwtUtils.isTokenValid(token)) {
            log.warn("Jwt Token está inválido ou expirado");
            // Se o token for inválido ou expirado, passa o controle para o próximo filtro.
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o nome de usuário do token JWT.
        String username = JwtUtils.getUsernameFromToken(token);

        // Autentica o usuário no contexto de segurança.
        toAuthentication(request, username);

        // Passa o controle para o próximo filtro da cadeia.
        filterChain.doFilter(request, response);
    }

    /**
     * Realiza a autenticação do usuário com base no nome de usuário extraído do token JWT.
     * Após autenticar o usuário, o contexto de segurança do Spring é atualizado com as credenciais do usuário.
     *
     * @param request  A requisição HTTP.
     * @param username O nome de usuário extraído do token JWT.
     */
    private void toAuthentication(HttpServletRequest request, String username) {
        // Carrega os detalhes do usuário (nome, senha, permissões) com base no nome de usuário.
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

        // Cria um token de autenticação com o usuário autenticado e suas permissões.
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        // Define os detalhes da requisição no objeto de autenticação.
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Atualiza o contexto de segurança do Spring com o usuário autenticado.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

/* Explicação do Código:
Campo jwtUserDetailsService:

Esse campo é injetado pelo Spring e contém o serviço responsável por carregar os detalhes do usuário a partir do nome de usuário (como as permissões e informações da conta). Ele é usado para autenticar o usuário quando o token JWT for válido.
Método doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain):

Esse método intercepta todas as requisições HTTP que passam pelo filtro.
Verificação do Token JWT:
Primeiro, o método obtém o token do cabeçalho "Authorization".
Verifica se o token está presente e se ele começa com o prefixo "Bearer ". Se não, o filtro passa a requisição para o próximo filtro na cadeia.
Se o token estiver presente, ele é validado usando o método JwtUtils.isTokenValid(token). Se for inválido ou expirado, o filtro continua a cadeia sem autenticação.
Autenticação do Usuário:
Se o token for válido, o nome de usuário é extraído do token usando JwtUtils.getUsernameFromToken(token).
O método toAuthentication(request, username) é então chamado para autenticar o usuário.
Método toAuthentication(HttpServletRequest request, String username):

Este método é responsável por realizar a autenticação no contexto de segurança do Spring.
Carregamento dos Detalhes do Usuário: O método jwtUserDetailsService.loadUserByUsername(username) é chamado para obter as informações do usuário com base no nome de usuário.
Criação do Token de Autenticação: Um objeto UsernamePasswordAuthenticationToken é criado, que contém o usuário autenticado e suas permissões.
Configuração do Contexto de Segurança: O token de autenticação é configurado no contexto de segurança do Spring (SecurityContextHolder), para que o usuário seja considerado autenticado nas próximas requisições.
Cadeia de Filtros (filterChain):

O filtro sempre passa o controle para o próximo filtro na cadeia, seja após autenticar o usuário ou quando o token não for válido.
Resumo:
Este código implementa um filtro de autenticação baseado em JWT. Ele verifica se um token JWT válido está presente no cabeçalho "Authorization" da requisição. Se o token for válido, o filtro autentica o usuário no contexto de segurança do Spring, permitindo o acesso a recursos protegidos.

Se houver falha na autenticação (por exemplo, um token ausente, inválido ou expirado), a requisição segue o fluxo normal sem autenticação.

Esse filtro é essencial em sistemas baseados em JWT para garantir que as requisições sejam autenticadas corretamente e seguras.
*/