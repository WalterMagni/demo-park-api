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

/**
 * Classe EstacionamentoService que fornece serviços relacionados ao gerenciamento de check-in e check-out em um estacionamento.
 * Esta classe contém métodos para realizar as operações de entrada e saída de veículos, interagindo com os serviços ClienteVagaService,
 * ClienteService e VagaService.
 * A anotação @Service marca esta classe como um componente de serviço gerenciado pelo Spring.
 */
@Service
public class EstacionamentoService {

    /**
     * Injeção do ClienteVagaService para operações relacionadas a ClienteVaga.
     */
    @Autowired
    private ClienteVagaService clienteVagaService;

    /**
     * Injeção do ClienteService para operações relacionadas ao Cliente.
     */
    @Autowired
    private ClienteService clienteService;

    /**
     * Injeção do VagaService para operações relacionadas a Vaga.
     */
    @Autowired
    private VagaService vagaService;

    /**
     * Realiza o processo de check-in de um cliente no estacionamento.
     * O método busca o cliente com base no CPF, encontra uma vaga livre, registra a data de entrada,
     * gera um recibo e salva a instância de ClienteVaga no banco de dados.
     *
     * @param clienteVaga A instância de ClienteVaga contendo as informações do check-in.
     * @return A instância de ClienteVaga salva com as informações de check-in preenchidas.
     */
    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga) {
        // Busca o cliente com base no CPF
        Cliente cliente = clienteService.findByCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        // Busca uma vaga livre e marca como ocupada
        Vaga vaga = vagaService.buscarProVagaLivre();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);

        // Define a vaga, data de entrada e gera um recibo
        clienteVaga.setVaga(vaga);
        clienteVaga.setDataEntrada(LocalDateTime.now());
        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

        // Salva a instância de ClienteVaga
        return clienteVagaService.save(clienteVaga);
    }

    /**
     * Realiza o processo de check-out de um cliente no estacionamento.
     * O método calcula o valor a ser pago com base na data de entrada e saída, aplica descontos,
     * e marca a vaga como livre após o check-out.
     *
     * @param recibo O recibo associado ao cliente que está realizando o check-out.
     * @return A instância de ClienteVaga salva com as informações de check-out atualizadas.
     */
    @Transactional
    public ClienteVaga checkOut(String recibo) {
        // Busca a instância de ClienteVaga com base no recibo
        ClienteVaga clienteVaga = clienteVagaService.findByRecibo(recibo);

        // Define a data de saída e calcula o valor
        LocalDateTime dataSaida = LocalDateTime.now();
        BigDecimal valor = EstacionamentoUtils.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);
        clienteVaga.setValor(valor);

        // Calcula o total de vezes que o cliente usou o estacionamento
        long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf());

        // Aplica desconto baseado no número de utilizações do estacionamento
        BigDecimal desconto = EstacionamentoUtils.calcularDesconto(valor, totalDeVezes);
        clienteVaga.setDesconto(desconto);

        // Atualiza a data de saída e marca a vaga como livre
        clienteVaga.setDataSaida(dataSaida);
        clienteVaga.getVaga().setStatus(Vaga.StatusVaga.LIVRE);

        // Salva a instância de ClienteVaga
        return clienteVagaService.save(clienteVaga);
    }
}

