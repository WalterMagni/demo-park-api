package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.Vaga;
import com.walter.demopark.repository.VagaRepository;
import com.walter.demopark.web.dto.vaga.VagaCreateDTO;
import com.walter.demopark.web.dto.vaga.VagaResponseDTO;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.matcher.StringMatcher;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDTO dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDTO toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDTO.class);
    }

}
