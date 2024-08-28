package com.walter.demopark.web.dto.mapper;

import com.walter.demopark.web.dto.pageable.PageableDto;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDto toDto(Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }



}
