package com.walter.demopark.service;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.exception.CpfUniqueViolationException;
import com.walter.demopark.exception.EntityNotFoundException;
import com.walter.demopark.repository.ClienteRepository;

import com.walter.demopark.repository.projection.ClienteProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe ClienteService que fornece serviços relacionados à entidade Cliente.
 * Esta classe contém métodos para realizar operações de leitura e gravação no banco de dados usando o ClienteRepository.
 * A anotação @Service marca esta classe como um componente de serviço gerenciado pelo Spring.
 */
@Service
public class ClienteService {

    /**
     * Injeção do ClienteRepository para acesso ao banco de dados.
     * O Spring automaticamente injeta uma instância de ClienteRepository.
     */
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Método para buscar uma lista paginada de projeções de Cliente.
     * A operação é marcada como somente leitura (readOnly = true) para garantir que não há alterações no banco de dados.
     *
     * @param pageable Objeto Pageable contendo informações sobre paginação e ordenação.
     * @return Uma página de ClienteProjection, conforme os parâmetros de paginação fornecidos.
     */
    @Transactional(readOnly = true)
    public Page<ClienteProjection> findAll(Pageable pageable) {
        return clienteRepository.findAllPageable(pageable);
    }

    /**
     * Busca um cliente pelo ID.
     * Se o cliente não for encontrado, uma exceção EntityNotFoundException será lançada.
     *
     * @param id O ID do cliente.
     * @return O cliente correspondente ao ID fornecido.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     */
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Cliente id=%d não encontrado", id)));
    }

    /**
     * Busca um cliente com base no ID do usuário associado.
     *
     * @param id O ID do usuário.
     * @return O cliente correspondente ao usuário fornecido.
     */
    @Transactional(readOnly = true)
    public Cliente findByUserId(Long id) {
        return clienteRepository.buscarPorClienteId(id);
    }

    /**
     * Salva um novo cliente no banco de dados.
     * Se o CPF já existir, uma exceção CpfUniqueViolationException será lançada.
     *
     * @param cliente O cliente a ser salvo.
     * @return O cliente salvo.
     * @throws CpfUniqueViolationException se o CPF já existir no banco de dados.
     */
    @Transactional
    public Cliente save(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(String.format("O cpf %s já existe no sistema", cliente.getCpf()));
        }
    }

    /**
     * Busca um cliente com base no CPF.
     * Se o cliente não for encontrado, uma exceção EntityNotFoundException será lançada.
     *
     * @param cpf O CPF do cliente.
     * @return O cliente correspondente ao CPF fornecido.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     */
    @Transactional(readOnly = true)
    public Cliente findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente com o cpf %s não encontrado", cpf)));
    }
}

