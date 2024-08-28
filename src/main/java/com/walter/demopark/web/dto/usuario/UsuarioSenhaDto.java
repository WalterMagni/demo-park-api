package com.walter.demopark.web.dto.usuario;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaDto {

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max=6, message = "A senha deve conter no mínimo 6 caracteres e no máximo 10")
    private String senhaAtual;
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max=6, message = "A senha deve conter no mínimo 6 caracteres e no máximo 10")
    private String novaSenha;
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max=6, message = "A senha deve conter no mínimo 6 caracteres e no máximo 10")
    private String confirmaSenha;

}
