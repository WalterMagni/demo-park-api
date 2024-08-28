package com.walter.demopark.service;

import com.walter.demopark.entity.Usuario;
import com.walter.demopark.exception.EntityNotFoundException;
import com.walter.demopark.exception.PasswordInvalidException;
import com.walter.demopark.exception.UsernameUniqueViolationException;
import com.walter.demopark.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    //GET
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id=%d não encontrado", id)));
    }

    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(String.format("Usuário %s não encontrado", username)));
    }

    //POST
    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("O nome de usuário %s já existe", usuario.getUsername()));
        }

    }

    //PUT
    @Transactional
    public Usuario updatePassword(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {

        if (!novaSenha.equals(confirmaSenha)) {
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
        }

        Usuario user = findById(id);
        if (!passwordEncoder.matches(senhaAtual, user.getPassword())) {
            throw new PasswordInvalidException("Sua senha não confere.");
        }

        user.setPassword(passwordEncoder.encode(novaSenha));
        return user;
    }

    //DELETE

    @Transactional(readOnly = true)
    public Usuario.Role findRoleByUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);

    }
}

