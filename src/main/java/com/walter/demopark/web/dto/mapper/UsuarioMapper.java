package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.Usuario;
import com.walter.demopark.web.dto.usuario.UsuarioCreateDto;
import com.walter.demopark.web.dto.usuario.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDto createDto) {
        return new ModelMapper().map(createDto, Usuario.class);
    }

    public static UsuarioResponseDto toDto(Usuario usuario) {

        String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    public static List<UsuarioResponseDto> toDto(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::toDto).toList();
    }


}
