package com.walter.demopark.service;

import com.walter.demopark.entity.Vaga;
import com.walter.demopark.exception.CodigoUniqueViolationException;
import com.walter.demopark.exception.EntityNotFoundException;
import com.walter.demopark.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe VagaService que fornece serviços relacionados à entidade Vaga.
 * Esta classe contém métodos para salvar, buscar por código e encontrar uma vaga livre no sistema.
 * A anotação @Service marca esta classe como um componente de serviço gerenciado pelo Spring.
 */
@Service
public class VagaService {

    /**
     * Injeção do VagaRepository para realizar operações no banco de dados relacionadas à entidade Vaga.
     */
    @Autowired
    private VagaRepository vagaRepository;

    /**
     * Salva uma nova vaga no banco de dados.
     * Se o código da vaga já existir, uma exceção CodigoUniqueViolationException será lançada.
     *
     * @param vaga O objeto Vaga a ser salvo.
     * @return O objeto Vaga salvo no banco de dados.
     * @throws CodigoUniqueViolationException se o código da vaga já existir no banco de dados.
     */
    @Transactional
    public Vaga save(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(String.format("O código %s já existe no sistema", vaga.getCodigo()));
        }
    }

    /**
     * Busca uma vaga com base no código fornecido. Se a vaga não for encontrada, uma exceção EntityNotFoundException será lançada.
     *
     * @param codigo O código da vaga a ser buscada.
     * @return A instância de Vaga correspondente ao código fornecido.
     * @throws EntityNotFoundException se a vaga não for encontrada.
     */
    @Transactional(readOnly = true)
    public Vaga findByCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Vaga %s não encontrada", codigo)));
    }

    /**
     * Busca a primeira vaga livre disponível no sistema.
     * Se nenhuma vaga livre for encontrada, uma exceção EntityNotFoundException será lançada.
     *
     * @return A instância de Vaga que está disponível (com status LIVRE).
     * @throws EntityNotFoundException se nenhuma vaga livre for encontrada.
     */
    public Vaga buscarProVagaLivre() {
        return vagaRepository.findFirstByStatus(Vaga.StatusVaga.LIVRE)
                .orElseThrow(() -> new EntityNotFoundException("Vaga livre não encontrada"));
    }
}
