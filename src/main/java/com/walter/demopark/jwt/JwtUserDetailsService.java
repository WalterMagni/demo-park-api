package com.walter.demopark.jwt;

import com.walter.demopark.entity.Usuario;
import com.walter.demopark.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioService.findByUsername(username);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String username) {
        Usuario.Role role = usuarioService.findRoleByUsername(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }

}
