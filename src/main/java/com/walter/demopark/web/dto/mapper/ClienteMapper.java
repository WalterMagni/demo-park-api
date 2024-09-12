package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.web.dto.cliente.ClienteCreateDto;
import com.walter.demopark.web.dto.cliente.ClienteResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * Classe utilitária ClienteMapper responsável por realizar a conversão entre diferentes representações do objeto Cliente.
 * Esta classe contém métodos estáticos que utilizam a biblioteca ModelMapper para transformar objetos de DTO para entidades e vice-versa.
 * A anotação @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE) impede a criação de instâncias desta classe,
 * uma vez que seus métodos são estáticos.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClienteMapper {

    /**
     * Converte um objeto ClienteCreateDto em uma entidade Cliente.
     * Utiliza o ModelMapper para mapear automaticamente os campos do DTO para a entidade correspondente.
     *
     * @param createDto O DTO que contém os dados para criar um Cliente.
     * @return Um objeto Cliente mapeado a partir do DTO fornecido.
     */
    public static Cliente toCliente(ClienteCreateDto createDto) {
        return new ModelMapper().map(createDto, Cliente.class);
    }

    /**
     * Converte uma entidade Cliente em um objeto ClienteResponseDto.
     * Utiliza o ModelMapper para mapear automaticamente os campos da entidade Cliente para o DTO correspondente.
     *
     * @param cliente A entidade Cliente que será convertida para DTO.
     * @return Um objeto ClienteResponseDto mapeado a partir da entidade Cliente.
     */
    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

    /**
     * Converte uma lista de entidades Cliente em uma lista de objetos ClienteResponseDto.
     * Utiliza o ModelMapper para mapear automaticamente os campos de cada Cliente para um DTO.
     *
     * @param clientes Uma lista de entidades Cliente.
     * @return Uma lista de objetos ClienteResponseDto mapeados a partir das entidades Cliente fornecidas.
     */
    public static List<ClienteResponseDto> toDto(List<Cliente> clientes) {
        return new ModelMapper().map(clientes, List.class);
    }
}

