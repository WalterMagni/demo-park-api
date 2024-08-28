package com.walter.demopark.jwt;

import com.walter.demopark.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Classe JwtUserDetails que representa os detalhes do usuário para o contexto de segurança JWT.
 * Extende UserDetails (geralmente em sistemas que usam Spring Security) e armazena informações
 * sobre o usuário autenticado.
 */

public class JwtUserDetails extends User {

    // Objeto Usuario representando o usuário autenticado
    private Usuario usuario;

    /**
     * Construtor que inicializa o JwtUserDetails com informações do usuário.
     *
     * @param usuario Objeto Usuario que contém as informações do usuário, como nome, senha e papel (role).
     */
    public JwtUserDetails(Usuario usuario) {
        // Chama o construtor da classe pai (User) com o nome de usuário, senha e a lista de autoridades (roles) do usuário.
        super(usuario.getUsername(), usuario.getPassword(),
                AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        // Armazena o objeto Usuario para uso futuro.
        this.usuario = usuario;
    }

    /**
     * Obtém o ID do usuário.
     *
     * @return O ID do usuário armazenado no objeto Usuario.
     */
    public Long getId() {
        return this.usuario.getId();
    }

    /**
     * Obtém o papel (role) do usuário.
     *
     * @return O nome do papel do usuário (em formato de string), que é armazenado no objeto Usuario.
     */
    public String getRole() {
        return this.usuario.getRole().name();
    }
}
