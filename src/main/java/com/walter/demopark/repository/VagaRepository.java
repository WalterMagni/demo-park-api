package com.walter.demopark.repository;

import com.walter.demopark.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface VagaRepository que estende JpaRepository.
 * Esta interface é responsável por realizar operações de acesso a dados relacionadas à entidade Vaga, utilizando a API JPA.
 * Ela contém métodos personalizados para consultar dados específicos no banco de dados, além de herdar operações CRUD da JpaRepository.
 */
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    /**
     * Busca uma vaga com base no código fornecido.
     * A implementação deste método é gerada automaticamente pelo Spring Data JPA com base na convenção de nomenclatura do método.
     *
     * @param codigo O código único da vaga para buscar.
     * @return Um Optional contendo a vaga correspondente ao código fornecido, ou vazio se não houver correspondência.
     */
    Optional<Vaga> findByCodigo(String codigo);

    /**
     * Busca a primeira vaga com o status fornecido.
     * A implementação deste método é gerada automaticamente pelo Spring Data JPA com base na convenção de nomenclatura do método.
     *
     * @param statusVaga O status da vaga para realizar a busca.
     * @return Um Optional contendo a primeira vaga encontrada com o status fornecido, ou vazio se não houver correspondência.
     */
    Optional<Vaga> findFirstByStatus(Vaga.StatusVaga statusVaga);
}

