package com.walter.demopark.web.controller;

import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.jwt.JwtUserDetails;
import com.walter.demopark.repository.projection.ClienteVagaProjection;
import com.walter.demopark.service.ClienteService;
import com.walter.demopark.service.ClienteVagaService;
import com.walter.demopark.service.EstacionamentoService;
import com.walter.demopark.service.JasperService;
import com.walter.demopark.web.dto.estacionamento.EstacionamentoCreateDto;
import com.walter.demopark.web.dto.estacionamento.EstacionamentoResponseDto;
import com.walter.demopark.web.dto.mapper.ClienteVagaMapper;
import com.walter.demopark.web.dto.mapper.PageableMapper;
import com.walter.demopark.web.dto.pageable.PageableDto;
import com.walter.demopark.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;

    @Autowired
    private ClienteVagaService clienteVagaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JasperService jasperService;

    /**
     * Operação de check-in.
     * Recurso para dar entrada de um veículo no estacionamento.
     * Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'.
     */
    @Operation(summary = "Operação de check-in",
            description = "Recurso para dar entrada de um veículo no estacionamento. "
                    + "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL de acesso ao recurso criado"),
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Causas possíveis: <br/>"
                            + "- CPF do cliente não cadastrado no sistema; <br/>"
                            + "- Nenhuma vaga livre foi localizada;",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkIn(@RequestBody @Valid EstacionamentoCreateDto dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    /**
     * Localizar um veículo estacionado.
     * Recurso para retornar um veículo estacionado pelo nº do recibo.
     * Requisição exige uso de um bearer token.
     */
    @Operation(summary = "Localizar um veículo estacionado",
            description = "Recurso para retornar um veículo estacionado pelo nº do recibo. "
                    + "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "recibo", description = "Número do recibo gerado pelo check-in")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Número do recibo não encontrado.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.findByRecibo(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    /**
     * Operação de check-out.
     * Recurso para dar saída de um veículo do estacionamento.
     * Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'.
     */
    @Operation(summary = "Operação de check-out",
            description = "Recurso para dar saída de um veículo do estacionamento. "
                    + "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "recibo", description = "Número do recibo gerado pelo check-in", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Número do recibo inexistente ou o veículo já passou pelo check-out.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkout(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    /**
     * Localizar os registros de estacionamentos do cliente por CPF.
     * Recurso para consultar os estacionamentos de um cliente pelo número do CPF.
     */
    @Operation(summary = "Localizar os registros de estacionamentos do cliente por CPF",
            description = "Recurso para consultar os registros de estacionamentos do cliente por CPF. "
                    + "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "cpf", description = "Número do CPF referente ao cliente a ser consultado", required = true),
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Representa a página retornada",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Representa o total de elementos por página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Campo padrão de ordenação 'dataEntrada,asc'.",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")), hidden = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PageableDto> getAllEstacionamentosProCpf(@PathVariable String cpf,
                                                                   @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projectionPage = clienteVagaService.findAllByClienteCpf(cpf, pageable);
        PageableDto dto = PageableMapper.toDto(projectionPage);
        return ResponseEntity.ok(dto);
    }

    /**
     * Localizar os registros de estacionamentos do cliente logado.
     * Requisição exige uso de um bearer token.
     */
    @Operation(summary = "Localizar os registros de estacionamentos do cliente logado",
            description = "Recurso para localizar os registros de estacionamentos do cliente logado. "
                    + "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Representa a página retornada",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Representa o total de elementos por página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de ADMIN.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping
    public ResponseEntity<PageableDto> getAllEstacionamentosDoCliente(@AuthenticationPrincipal JwtUserDetails user,
                                                                      @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projectionPage = clienteVagaService.findAllByUsuarioId(user.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projectionPage);
        return ResponseEntity.ok(dto);
    }

    /**
     * Gera um relatório PDF para o cliente autenticado.
     * <p>
     * Este endpoint é acessível apenas para usuários com a role 'CLIENTE'. O relatório é gerado
     * com base no CPF do cliente associado ao usuário autenticado. O relatório é retornado como um
     * arquivo PDF inline no corpo da resposta HTTP.
     * </p>
     *
     * @param response HttpServletResponse onde o relatório PDF será escrito.
     * @param user     O usuário autenticado, contendo as informações necessárias para buscar o cliente.
     * @return ResponseEntity<Void> Retorna uma resposta HTTP com o status 200 (OK).
     * @throws IOException Se ocorrer algum erro ao escrever o relatório no fluxo de saída da resposta.
     */
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/relatorio")
    public ResponseEntity<Void> getRelatorio(HttpServletResponse response,
                                             @AuthenticationPrincipal JwtUserDetails user) throws IOException {

        // Obtém o CPF do cliente associado ao usuário autenticado.
        String cpf = clienteService.findByUserId(user.getId()).getCpf();

        // Adiciona o CPF como parâmetro para o relatório Jasper
        jasperService.addParams("CPF", cpf);

        // Gera o relatório em formato PDF
        byte[] bytes = jasperService.generateReport();

        // Configura o tipo de conteúdo da resposta como PDF e define o cabeçalho para exibição inline
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + System.currentTimeMillis() + ".pdf");

        // Escreve o conteúdo do PDF no fluxo de saída da resposta
        response.getOutputStream().write(bytes);

        // Retorna uma resposta HTTP 200 OK sem conteúdo adicional
        return ResponseEntity.ok().build();
    }

}

