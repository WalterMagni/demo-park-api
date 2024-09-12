package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.web.dto.estacionamento.EstacionamentoCreateDto;
import com.walter.demopark.web.dto.estacionamento.EstacionamentoResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * Classe utilitária ClienteVagaMapper responsável por realizar a conversão entre diferentes representações do objeto ClienteVaga.
 * Esta classe contém métodos estáticos que utilizam a biblioteca ModelMapper para transformar objetos de DTO para entidades e vice-versa.
 * A anotação @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE) impede a criação de instâncias desta classe,
 * uma vez que seus métodos são estáticos.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    /**
     * Converte um objeto EstacionamentoCreateDto em uma entidade ClienteVaga.
     * Utiliza o ModelMapper para mapear automaticamente os campos do DTO para a entidade correspondente.
     *
     * @param dto O DTO que contém os dados para criar uma instância de ClienteVaga.
     * @return Um objeto ClienteVaga mapeado a partir do DTO fornecido.
     */
    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    /**
     * Converte uma entidade ClienteVaga em um objeto EstacionamentoResponseDto.
     * Utiliza o ModelMapper para mapear automaticamente os campos da entidade ClienteVaga para o DTO correspondente.
     *
     * @param clienteVaga A entidade ClienteVaga que será convertida para DTO.
     * @return Um objeto EstacionamentoResponseDto mapeado a partir da entidade ClienteVaga.
     */
    public static EstacionamentoResponseDto toDto(ClienteVaga clienteVaga) {
        return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
    }
}

