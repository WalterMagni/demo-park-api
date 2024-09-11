package com.walter.demopark.service;

import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.repository.ClienteVagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteVagaService {

    @Autowired
    private ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga save(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }



}
