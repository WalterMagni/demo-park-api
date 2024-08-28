package com.walter.demopark.web.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UsuarioCreateDto {

    @NotBlank(message = "O email de usuário é obrigatório")
    @Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "formato do e-mail inválido")
    private String username;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max=10, message = "A senha deve conter no mínimo 6 caracteres e no máximo 10")
    private String password;

}
