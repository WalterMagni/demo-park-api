package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.web.dto.estacionamento.EstacionamentoCreateDto;
import com.walter.demopark.web.dto.estacionamento.EstacionamentoResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toDto(ClienteVaga clienteVaga) {
        return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
    }
}
