package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.web.dto.pageable.PageableDto;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

/**
 * Classe utilitária PageableMapper responsável por realizar a conversão de objetos Page para PageableDto.
 * Esta classe contém métodos estáticos que utilizam a biblioteca ModelMapper para transformar objetos de paginação em DTOs.
 * A anotação @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE) impede a criação de instâncias desta classe,
 * uma vez que seus métodos são estáticos.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PageableMapper {

    /**
     * Converte um objeto Page em um PageableDto.
     * Utiliza o ModelMapper para mapear automaticamente os campos da classe Page para o DTO correspondente.
     *
     * @param page O objeto Page que contém as informações de paginação.
     * @return Um objeto PageableDto mapeado a partir do objeto Page fornecido.
     */
    public static PageableDto toDto(Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}

