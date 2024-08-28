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

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    //GET
    @Transactional(readOnly = true)
    public Page<ClienteProjection> findAll(Pageable pageable) {
        return clienteRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Cliente id=%d não encontrado", id)));
    }

    //POST
    @Transactional
    public Cliente save(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(String.format("O cpf %s já existe no sistema", cliente.getCpf()));
        }
    }
}
