package com.walter.demopark.web.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateDto {

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(min = 3, max = 100, message = "O nome do cliente deve conter no mínimo 3 e no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O cpf do cliente é obrigatório")
    @Size(min = 11, max = 11, message = "O cpf do cliente deve conter 11 dígitos")
    @CPF
    private String cpf;

}
