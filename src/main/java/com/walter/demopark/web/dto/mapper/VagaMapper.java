package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.entity.Vaga;
import com.walter.demopark.web.dto.vaga.VagaCreateDto;
import com.walter.demopark.web.dto.vaga.VagaResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDto toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }

}
