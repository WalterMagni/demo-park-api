package com.walter.demopark.repository;

import com.walter.demopark.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Interface UsuarioRepository que estende JpaRepository.
 * Esta interface é responsável por realizar operações de acesso a dados relacionadas à entidade Usuario, utilizando a API JPA.
 * Ela contém métodos personalizados para consultar dados específicos no banco de dados, além de herdar operações CRUD da JpaRepository.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo nome de usuário.
     * A implementação deste método é gerada automaticamente pelo Spring Data JPA com base na convenção de nomenclatura do método.
     *
     * @param username O nome de usuário para buscar.
     * @return Um Optional contendo o usuário correspondente ao nome fornecido, ou vazio se não houver correspondência.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca o papel (role) de um usuário com base no nome de usuário.
     * Essa consulta utiliza JPQL para selecionar apenas o campo 'role' da entidade Usuario.
     *
     * @param username O nome de usuário para buscar o papel (role).
     * @return O papel (role) do usuário correspondente ao nome fornecido.
     */
    @Query("select u.role from Usuario u where u.username like :username")
    Usuario.Role findRoleByUsername(String username);
}
