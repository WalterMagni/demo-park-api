package com.walter.demopark.web.controller;

import com.walter.demopark.entity.Usuario;
import com.walter.demopark.service.UsuarioService;
import com.walter.demopark.web.dto.usuario.UsuarioCreateDto;
import com.walter.demopark.web.dto.usuario.UsuarioResponseDto;
import com.walter.demopark.web.dto.usuario.UsuarioSenhaDto;
import com.walter.demopark.web.dto.mapper.UsuarioMapper;
import com.walter.demopark.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Contem todas as operações de usuário")
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Buscar todos os Usuários", description = "Requisição exige um Bearer Token, acesso restrito a ADMIN e CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> findAll() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(UsuarioMapper.toDto(usuarios));
    }

    @Operation(summary = "Buscar Usuario por Id", description = "Requisição exige um Bearer Token, acesso restrito a ADMIN e CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE') and #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> findById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(usuario));
    }

    @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados de Entrada Invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.save(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Atualiza a senha de um usuário", description = "Requisição exige um Bearer Token, acesso restrito a ADMIN e CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Senha atual não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') and (#id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> updatePassword(@Valid @PathVariable Long id, @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.updatePassword(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }


}
