package com.walter.demopark.web.controller;

import com.walter.demopark.jwt.JwtToken;
import com.walter.demopark.jwt.JwtUserDetailsService;
import com.walter.demopark.web.dto.usuario.UsuarioLoginDto;
import com.walter.demopark.web.dto.usuario.UsuarioResponseDto;
import com.walter.demopark.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável pela autenticação de usuários na API.
 *
 * Este controlador expõe um endpoint para autenticar usuários, gerando um token JWT em caso de sucesso.
 * Ele utiliza o Spring Security para gerenciar o processo de autenticação e validação das credenciais.
 * O token JWT retornado pode ser usado para autorizar o acesso a recursos protegidos na API.
 */
@Tag(name = "Autenticação", description = "Recurso para proceder a autenticação de usuários")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AutenticacaoController {

    // Serviço responsável por carregar os detalhes do usuário e gerar o token JWT
    private final JwtUserDetailsService jwtUserDetailsService;

    // Gerenciador de autenticação do Spring Security
    private final AuthenticationManager authenticationManager;

    /**
     * Autentica um usuário na API.
     *
     * Este método recebe um DTO contendo as credenciais do usuário (nome de usuário e senha), realiza a autenticação
     * através do Spring Security, e, em caso de sucesso, retorna um token JWT que pode ser usado para autorizar
     * requisições subsequentes. Se a autenticação falhar, uma mensagem de erro é retornada.
     *
     * @param dto Objeto DTO contendo o nome de usuário e a senha.
     * @param request Objeto HttpServletRequest para capturar informações da requisição.
     * @return ResponseEntity contendo o token JWT em caso de sucesso ou uma mensagem de erro em caso de falha.
     */
    @Operation(
            summary = "Autenticar na API",
            description = "Recurso de autenticação na API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário autenticado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais inválidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticação iniciado para o usuário: {}", dto.getUsername());
        try {
            // Cria um token de autenticação com as credenciais fornecidas
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            // Realiza a autenticação usando o AuthenticationManager
            authenticationManager.authenticate(token);

            // Se a autenticação for bem-sucedida, gera um token JWT para o usuário
            JwtToken jwtToken = jwtUserDetailsService.getTokenAuthenticated(dto.getUsername());

            // Retorna o token JWT no corpo da resposta com status HTTP 200 (OK)
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationException e) {
            // Se ocorrer uma falha de autenticação, registra o erro e retorna uma mensagem de erro
            log.warn("Erro na autenticação do usuário: {}", dto.getUsername(), e);
        }

        // Se a autenticação falhar, retorna um erro HTTP 400 (Bad Request) com uma mensagem adequada
        return ResponseEntity.badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Usuário ou senha inválido"));
    }
}
