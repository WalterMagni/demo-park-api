package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.Vaga;
import com.walter.demopark.web.dto.vaga.VagaCreateDto;
import com.walter.demopark.web.dto.vaga.VagaResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * Classe utilitária VagaMapper responsável por realizar a conversão entre diferentes representações da entidade Vaga.
 * Esta classe contém métodos estáticos que utilizam a biblioteca ModelMapper para transformar objetos de DTO para entidades
 * e vice-versa. A anotação @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE) impede a criação de instâncias desta classe,
 * uma vez que seus métodos são estáticos.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class VagaMapper {

    /**
     * Converte um objeto VagaCreateDto em uma entidade Vaga.
     * Utiliza o ModelMapper para mapear automaticamente os campos do DTO para a entidade correspondente.
     *
     * @param dto O DTO que contém os dados para criar uma Vaga.
     * @return Um objeto Vaga mapeado a partir do DTO fornecido.
     */
    public static Vaga toVaga(VagaCreateDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    /**
     * Converte uma entidade Vaga em um objeto VagaResponseDto.
     * Utiliza o ModelMapper para mapear automaticamente os campos da entidade Vaga para o DTO correspondente.
     *
     * @param vaga A entidade Vaga que será convertida para DTO.
     * @return Um objeto VagaResponseDto mapeado a partir da entidade Vaga.
     */
    public static VagaResponseDto toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}

