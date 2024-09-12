package com.walter.demopark.jwt;

import com.walter.demopark.entity.Usuario;
import com.walter.demopark.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Classe JwtUserDetailsService que implementa a interface UserDetailsService.
 * Esta classe é um serviço responsável por fornecer detalhes do usuário (UserDetails) com base em um nome de usuário.
 *
 * A anotação @RequiredArgsConstructor, do framework Lombok, é utilizada para gerar automaticamente um construtor
 * que inicializa os campos finais (final) da classe.
 * A anotação @Service marca a classe como um componente de serviço no contexto do Spring, permitindo sua injeção e gerenciamento pelo Spring Framework.
 */
@RequiredArgsConstructor // Gera automaticamente um construtor para os campos finais
@Service                 // Declara que esta classe é um serviço Spring
public class JwtUserDetailsService implements UserDetailsService {

    /**
     * Dependência do serviço UsuarioService, usada para buscar dados relacionados ao usuário.
     * É injetada automaticamente através do construtor gerado pelo Lombok devido à anotação @RequiredArgsConstructor.
     */
    private final UsuarioService usuarioService;

    /**
     * Carrega os detalhes do usuário com base no nome de usuário fornecido.
     * Este método é utilizado pelo Spring Security durante o processo de autenticação.
     *
     * @param username O nome de usuário para buscar os detalhes.
     * @return Um objeto UserDetails que contém as informações do usuário, encapsuladas em um JwtUserDetails.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Busca o usuário pelo nome de usuário utilizando o serviço UsuarioService
        Usuario usuario = usuarioService.findByUsername(username);
        // Retorna um objeto JwtUserDetails, que implementa UserDetails
        return new JwtUserDetails(usuario);
    }

    /**
     * Gera um token JWT autenticado para o usuário com base no nome de usuário fornecido.
     * Este método busca a role do usuário e utiliza a utilidade JwtUtils para criar o token JWT.
     *
     * @param username O nome de usuário para gerar o token.
     * @return Um objeto JwtToken que contém o token JWT gerado.
     */
    public JwtToken getTokenAuthenticated(String username) {
        // Busca a role do usuário utilizando o serviço UsuarioService
        Usuario.Role role = usuarioService.findRoleByUsername(username);
        // Cria e retorna um token JWT, removendo o prefixo "ROLE_" do nome da role
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }

}
