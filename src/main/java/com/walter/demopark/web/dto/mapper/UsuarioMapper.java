package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.Usuario;
import com.walter.demopark.web.dto.usuario.UsuarioCreateDto;
import com.walter.demopark.web.dto.usuario.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;

/**
 * Classe utilitária UsuarioMapper responsável por realizar a conversão entre diferentes representações do objeto Usuario.
 * Esta classe contém métodos estáticos que utilizam a biblioteca ModelMapper para transformar objetos de DTO para entidades
 * e vice-versa, incluindo o mapeamento personalizado para a role do usuário.
 */
public class UsuarioMapper {

    /**
     * Converte um objeto UsuarioCreateDto em uma entidade Usuario.
     * Utiliza o ModelMapper para mapear automaticamente os campos do DTO para a entidade correspondente.
     *
     * @param createDto O DTO que contém os dados para criar um Usuario.
     * @return Um objeto Usuario mapeado a partir do DTO fornecido.
     */
    public static Usuario toUsuario(UsuarioCreateDto createDto) {
        return new ModelMapper().map(createDto, Usuario.class);
    }

    /**
     * Converte uma entidade Usuario em um objeto UsuarioResponseDto, com mapeamento personalizado para a role.
     * A role é extraída removendo o prefixo "ROLE_" antes de ser mapeada para o DTO.
     *
     * @param usuario A entidade Usuario que será convertida para DTO.
     * @return Um objeto UsuarioResponseDto mapeado a partir da entidade Usuario.
     */
    public static UsuarioResponseDto toDto(Usuario usuario) {
        // Extrai a role removendo o prefixo "ROLE_"
        String role = usuario.getRole().name().substring("ROLE_".length());

        // Define o mapeamento personalizado para a role
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role); // Configura o mapeamento da role personalizada
            }
        };

        // Cria uma instância de ModelMapper e aplica o mapeamento personalizado
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    /**
     * Converte uma lista de entidades Usuario em uma lista de objetos UsuarioResponseDto.
     * Utiliza o método toDto para mapear cada Usuario individualmente.
     *
     * @param usuarios Uma lista de entidades Usuario.
     * @return Uma lista de objetos UsuarioResponseDto mapeados a partir das entidades Usuario fornecidas.
     */
    public static List<UsuarioResponseDto> toDto(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::toDto).toList();
    }
}

