package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.web.dto.cliente.ClienteCreateDto;
import com.walter.demopark.web.dto.cliente.ClienteResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto createDto) {
        return new ModelMapper().map(createDto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

    public static List<ClienteResponseDto> toDto(List<Cliente> clientes) {
        return new ModelMapper().map(clientes, List.class);
    }

}
