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

/**
 * Classe UsuarioService que fornece serviços relacionados à entidade Usuario.
 * Esta classe contém métodos para realizar operações de leitura, gravação, atualização e exclusão de dados de usuários,
 * utilizando o UsuarioRepository e gerenciando a codificação de senhas.
 * A anotação @Service marca esta classe como um componente de serviço gerenciado pelo Spring.
 */
@Service
public class UsuarioService {

    /**
     * Injeção do UsuarioRepository para realizar operações no banco de dados relacionadas ao usuário.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Injeção do PasswordEncoder para codificação e verificação de senhas.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Métodos GET

    /**
     * Busca e retorna uma lista de todos os usuários cadastrados no sistema.
     * A operação é marcada como somente leitura (readOnly = true) para garantir que não há alterações no banco de dados.
     *
     * @return Uma lista de todos os usuários.
     */
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca e retorna um usuário pelo ID. Se o usuário não for encontrado, uma exceção EntityNotFoundException será lançada.
     *
     * @param id O ID do usuário a ser buscado.
     * @return O usuário correspondente ao ID fornecido.
     * @throws EntityNotFoundException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id=%d não encontrado", id)));
    }

    /**
     * Busca e retorna um usuário pelo nome de usuário. Se o usuário não for encontrado, uma exceção EntityNotFoundException será lançada.
     *
     * @param username O nome de usuário a ser buscado.
     * @return O usuário correspondente ao nome de usuário fornecido.
     * @throws EntityNotFoundException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(String.format("Usuário %s não encontrado", username)));
    }

    /**
     * Salva um novo usuário no banco de dados, codificando sua senha antes de salvar.
     * Se o nome de usuário já existir, uma exceção UsernameUniqueViolationException será lançada.
     *
     * @param usuario O objeto Usuario a ser salvo.
     * @return O objeto Usuario salvo no banco de dados.
     * @throws UsernameUniqueViolationException se o nome de usuário já existir no banco de dados.
     */
    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("O nome de usuário %s já existe", usuario.getUsername()));
        }
    }

    /**
     * Atualiza a senha de um usuário com base em seu ID, verificando a senha atual e a confirmação da nova senha.
     * Lança exceções se a senha atual não for válida ou se a nova senha não coincidir com a confirmação.
     *
     * @param id O ID do usuário.
     * @param senhaAtual A senha atual do usuário.
     * @param novaSenha A nova senha a ser atualizada.
     * @param confirmaSenha A confirmação da nova senha.
     * @return O usuário com a senha atualizada.
     * @throws PasswordInvalidException se a senha atual for inválida ou se a nova senha não coincidir com a confirmação.
     */
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

    /**
     * Busca e retorna o papel (role) de um usuário com base em seu nome de usuário.
     *
     * @param username O nome de usuário para buscar o papel.
     * @return O papel (role) correspondente ao nome de usuário fornecido.
     */
    @Transactional(readOnly = true)
    public Usuario.Role findRoleByUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}


