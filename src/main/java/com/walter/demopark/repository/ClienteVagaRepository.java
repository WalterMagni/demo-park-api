package com.walter.demopark.repository;

import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.repository.projection.ClienteVagaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface ClienteVagaRepository que estende JpaRepository.
 * Esta interface é responsável por realizar operações de acesso a dados relacionadas à entidade ClienteVaga, utilizando a API JPA.
 * Ela contém métodos personalizados para consultar dados específicos no banco de dados, além de herdar operações CRUD da JpaRepository.
 */
public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {

    /**
     * Busca uma instância de ClienteVaga cujo recibo corresponda ao fornecido e que não tenha data de saída (vaga ainda ativa).
     * A implementação deste método é gerada automaticamente pelo Spring Data JPA com base na nomenclatura do método.
     *
     * @param recibo O número do recibo associado à vaga do cliente.
     * @return Um Optional que pode conter um ClienteVaga com o recibo fornecido e data de saída nula, ou estar vazio se não houver correspondência.
     */
    Optional<ClienteVaga> findByReciboAndDataSaidaIsNull(String recibo);

    /**
     * Conta o número de vagas que um cliente, identificado pelo CPF, utilizou e já tem uma data de saída (vaga já finalizada).
     * A implementação é gerada automaticamente pelo Spring Data JPA.
     *
     * @param cpf O CPF do cliente.
     * @return O número de vagas que o cliente com o CPF fornecido já utilizou e tem data de saída preenchida.
     */
    long countByClienteCpfAndDataSaidaIsNotNull(String cpf);

    /**
     * Retorna uma página de projeções ClienteVagaProjection, filtrada pelo CPF do cliente.
     * A paginação é controlada pelo parâmetro Pageable, permitindo resultados paginados.
     *
     * @param cpf O CPF do cliente para filtrar as vagas.
     * @param pageable Objeto Pageable que contém as informações de paginação.
     * @return Uma página de ClienteVagaProjection correspondente ao CPF fornecido.
     */
    Page<ClienteVagaProjection> findAllByClienteCpf(String cpf, Pageable pageable);

    /**
     * Retorna uma página de projeções ClienteVagaProjection, filtrada pelo ID do usuário associado ao cliente.
     * A paginação é controlada pelo parâmetro Pageable, permitindo resultados paginados.
     *
     * @param id O ID do usuário associado ao cliente.
     * @param pageable Objeto Pageable que contém as informações de paginação.
     * @return Uma página de ClienteVagaProjection correspondente ao ID de usuário fornecido.
     */
    Page<ClienteVagaProjection> findAllByClienteUsuarioId(Long id, Pageable pageable);
}

