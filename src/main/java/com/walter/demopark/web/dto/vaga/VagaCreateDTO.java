package com.walter.demopark.web.dto.vaga;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VagaCreateDTO {

    @NotBlank
    @Size(min = 4, max = 4)
    private String codigo;
    @NotBlank
    @Pattern(regexp = "LIBERADA|OCUPADA")
    private String status;
}
