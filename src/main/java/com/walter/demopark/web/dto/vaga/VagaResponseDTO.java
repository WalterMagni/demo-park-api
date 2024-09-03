package com.walter.demopark.web.dto.vaga;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VagaResponseDTO {

    private Long id;
    private String codigo;
    private String status;

}
