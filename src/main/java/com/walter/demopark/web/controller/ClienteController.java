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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "Clientes", description = "Contem todas as operações de cliente")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private UsuarioService usuarioService;



    @Operation(summary = "Buscar todos os Usuarios", description = "Requisição exige um Bearer Token, acesso restrito a ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "clientes encontrados com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAll(Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }

    @Operation(summary = "Buscar Cliente por Id", description = "Requisição exige um Bearer Token, acesso restrito a ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> findById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }


    @Operation(summary = "Cria um novo cliente", description = "Cria um novo cliente, Requisição exige um Bearer Token, acesso restrito a ADMIN",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não Permitido ao perfil de ADMIN",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente com CPF já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados de Entrada Invalidos",
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
