package com.walter.demopark.service;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.entity.ClienteVaga;
import com.walter.demopark.entity.Vaga;
import com.walter.demopark.util.EstacionamentoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Transactional
    public ClienteVaga checkOut(String recibo) {

        ClienteVaga clienteVaga = clienteVagaService.findByRecibo(recibo);

        LocalDateTime dataSaida = LocalDateTime.now();

        BigDecimal valor = EstacionamentoUtils.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);
        clienteVaga.setValor(valor);

        long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf());

        BigDecimal desconto = EstacionamentoUtils.calcularDesconto(valor, totalDeVezes);
        clienteVaga.setDesconto(desconto);


        clienteVaga.setDataSaida(dataSaida);
        clienteVaga.getVaga().setStatus(Vaga.StatusVaga.LIVRE);

        return clienteVagaService.save(clienteVaga);
    }
}
