package com.walter.demopark.web.controller;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.jwt.JwtUserDetails;
import com.walter.demopark.repository.projection.ClienteProjection;
import com.walter.demopark.service.ClienteService;
import com.walter.demopark.service.UsuarioService;
import com.walter.demopark.web.dto.cliente.ClienteCreateDto;
import com.walter.demopark.web.dto.cliente.ClienteResponseDto;
import com.walter.demopark.web.dto.mapper.ClienteMapper;
import com.walter.demopark.web.dto.mapper.PageableMapper;
import com.walter.demopark.web.dto.pageable.PageableDto;
import com.walter.demopark.web.dto.usuario.UsuarioResponseDto;
import com.walter.demopark.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

/**
 * Controlador ClienteController que expõe endpoints relacionados à entidade Cliente.
 * Esta classe é anotada com @RestController para indicar que trata requisições HTTP e
 * @RequestMapping para mapear o caminho base "/api/v1/clientes".
 * Além disso, está documentada para OpenAPI/Swagger usando @Tag e @Operation, detalhando os endpoints e suas funcionalidades.
 */
@Tag(name = "Clientes", description = "Contém todas as operações de cliente")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    /**
     * Injeção do ClienteService para realizar operações relacionadas a Cliente.
     */
    @Autowired
    private ClienteService clienteService;

    /**
     * Injeção do UsuarioService para operações relacionadas a Usuário.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para recuperar uma lista paginada de clientes.
     * Apenas usuários com o perfil ADMIN têm acesso a este endpoint.
     *
     * @param pageable Objeto Pageable contendo informações de paginação e ordenação.
     * @return Um ResponseEntity contendo a lista paginada de clientes.
     */
    @Operation(summary = "Recuperar lista de clientes",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o total de elementos por página"),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "nome,asc")),
                            description = "Representa a ordenação dos resultados. Aceita múltiplos critérios de ordenação.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"nome", "asc"}) Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }

    /**
     * Endpoint para buscar os detalhes de um cliente logado (perfil CLIENTE).
     * Exige que o usuário esteja autenticado e que o perfil seja CLIENTE.
     *
     * @param jwtUserDetails Detalhes do usuário autenticado.
     * @return Um ResponseEntity contendo os detalhes do cliente.
     */
    @Operation(summary = "Buscar Cliente por Id", description = "Requisição exige um Bearer Token, acesso restrito a CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getclientDetails(@AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        Cliente cliente = clienteService.findByUserId(jwtUserDetails.getId());
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    /**
     * Endpoint para criar um novo cliente. Acesso restrito a usuários com perfil ADMIN.
     * Exige um Bearer Token e validação dos dados fornecidos.
     *
     * @param dto O DTO contendo os dados necessários para criar um cliente.
     * @param userDetails Detalhes do usuário autenticado.
     * @return Um ResponseEntity contendo os detalhes do cliente criado.
     */
    @Operation(summary = "Cria um novo cliente", description = "Cria um novo cliente, Requisição exige um Bearer Token, acesso restrito a ADMIN",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não Permitido ao perfil de ADMIN",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente com CPF já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados de Entrada Inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@Valid @RequestBody ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.findById(userDetails.getId()));

        clienteService.save(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }
}

