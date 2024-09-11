package com.walter.demopark.service;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.entity.Vaga;
import com.walter.demopark.util.EstacionamentoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EstacionamentoService {

    @Autowired
    private ClienteVagaService clienteVagaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VagaService vagaService;

    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga) {

        Cliente cliente = clienteService.findByCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.buscarProVagaLivre();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);

        clienteVaga.setVaga(vaga);
        clienteVaga.setDataEntrada(LocalDateTime.now());
        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

        return clienteVagaService.save(clienteVaga);
    }

}
