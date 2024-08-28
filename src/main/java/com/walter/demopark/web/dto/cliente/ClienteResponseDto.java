package com.walter.demopark.web.dto.cliente;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClienteResponseDto {

    private Long id;
    private String nome;
    private String cpf;
}
