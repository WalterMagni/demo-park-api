package com.walter.demopark.web.controller;

import com.walter.demopark.entity.Vaga;
import com.walter.demopark.service.VagaService;
import com.walter.demopark.web.dto.mapper.VagaMapper;
import com.walter.demopark.web.dto.vaga.VagaCreateDto;
import com.walter.demopark.web.dto.vaga.VagaResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vagas", description = "Contem todas as operações de vagas")
@RestController
@RequestMapping("/api/v1/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto dto) {
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.save(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.findByCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }
}
