package com.walter.demopark.service;

import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.exception.EntityNotFoundException;
import com.walter.demopark.repository.ClienteVagaRepository;
import com.walter.demopark.repository.projection.ClienteVagaProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe ClienteVagaService que fornece serviços relacionados à entidade ClienteVaga.
 * Esta classe contém métodos para realizar operações de leitura e gravação no banco de dados utilizando o ClienteVagaRepository.
 * A anotação @Service marca esta classe como um componente de serviço gerenciado pelo Spring.
 */
@Service
public class ClienteVagaService {

    /**
     * Injeção do ClienteVagaRepository para acesso ao banco de dados.
     * O Spring automaticamente injeta uma instância de ClienteVagaRepository.
     */
    @Autowired
    private ClienteVagaRepository clienteVagaRepository;

    /**
     * Salva uma nova instância de ClienteVaga no banco de dados.
     *
     * @param clienteVaga A instância de ClienteVaga a ser salva.
     * @return A instância de ClienteVaga salva.
     */
    @Transactional
    public ClienteVaga save(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }

    /**
     * Busca uma instância de ClienteVaga com base no recibo, verificando se a data de saída é nula (vaga ainda ativa).
     * Se a vaga não for encontrada ou se já houver um check-out, uma exceção EntityNotFoundException é lançada.
     *
     * @param recibo O recibo da vaga do cliente.
     * @return A instância de ClienteVaga correspondente ao recibo fornecido.
     * @throws EntityNotFoundException se o recibo não for encontrado ou a data de saída não for nula.
     */
    @Transactional(readOnly = true)
    public ClienteVaga findByRecibo(String recibo) {
        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recibo %s não encontrado ou check-out já realizado", recibo)));
    }

    /**
     * Conta o número de vezes que o cliente, identificado pelo CPF, utilizou o estacionamento e completou o check-out (data de saída preenchida).
     *
     * @param cpf O CPF do cliente.
     * @return O número de vezes que o cliente utilizou o estacionamento e completou o check-out.
     */
    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    /**
     * Retorna uma página de projeções ClienteVagaProjection, filtrada pelo CPF do cliente.
     * A paginação é controlada pelo objeto Pageable, permitindo resultados paginados.
     *
     * @param cpf O CPF do cliente.
     * @param pageable Objeto Pageable contendo informações de paginação.
     * @return Uma página de ClienteVagaProjection correspondente ao CPF fornecido.
     */
    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> findAllByClienteCpf(String cpf, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteCpf(cpf, pageable);
    }

    /**
     * Retorna uma página de projeções ClienteVagaProjection, filtrada pelo ID do usuário associado ao cliente.
     * A paginação é controlada pelo objeto Pageable, permitindo resultados paginados.
     *
     * @param id O ID do usuário.
     * @param pageable Objeto Pageable contendo informações de paginação.
     * @return Uma página de ClienteVagaProjection correspondente ao ID do usuário fornecido.
     */
    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> findAllByUsuarioId(Long id, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteUsuarioId(id, pageable);
    }
}

