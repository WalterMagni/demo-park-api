package com.walter.demopark.repository;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.repository.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface ClienteRepository que estende JpaRepository.
 * Esta interface atua como um repositório de dados para a entidade Cliente, fornecendo métodos padrão e personalizados
 * para interagir com o banco de dados. Ela utiliza a API JPA para realizar operações CRUD e consultas customizadas.
 *
 * A anotação @Repository marca esta interface como um componente Spring, permitindo que o Spring a detecte
 * e injete suas instâncias onde necessário. O Spring Data JPA gerencia automaticamente a implementação dos métodos.
 */
@Repository  // Marca a interface como um repositório do Spring Data JPA
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Consulta personalizada que retorna uma página de objetos do tipo ClienteProjection.
     * Esta consulta utiliza JPQL para selecionar todos os clientes.
     *
     * @param pageable Objeto Pageable que contém as informações de paginação.
     * @return Uma página contendo projeções de Cliente.
     */
    @Query("select c from Cliente c")
    Page<ClienteProjection> findAllPageable(Pageable pageable);

    /**
     * Consulta personalizada que busca um cliente com base no ID do usuário associado.
     * Essa consulta utiliza JPQL para selecionar um cliente cujo usuário tenha o ID fornecido.
     *
     * @param id O ID do usuário associado ao cliente.
     * @return Um objeto Cliente correspondente ao ID do usuário fornecido.
     */
    @Query("SELECT c FROM Cliente c WHERE c.usuario.id = :id")
    Cliente buscarPorClienteId(@Param("id") Long id);

    /**
     * Método que busca um cliente com base no CPF.
     * O Spring Data JPA gera automaticamente a implementação dessa consulta com base na convenção de nomenclatura do método.
     *
     * @param cpf O CPF do cliente.
     * @return Um Optional que pode conter o Cliente correspondente ao CPF fornecido, ou estar vazio se não houver correspondência.
     */
    Optional<Cliente> findByCpf(String cpf);
}

