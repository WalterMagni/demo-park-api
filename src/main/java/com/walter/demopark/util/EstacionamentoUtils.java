package com.walter.demopark.util;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Classe utilitária EstacionamentoUtils que fornece métodos estáticos para auxiliar nas operações de um sistema de estacionamento.
 * Esta classe contém métodos para gerar recibos, calcular o custo de estacionamento e aplicar descontos com base no número de utilizações.
 * A anotação @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE) impede a criação de instâncias desta classe, já que todos os métodos são estáticos.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    /**
     * Valor cobrado para os primeiros 15 minutos de estacionamento.
     */
    private static final double PRIMEIROS_15_MINUTES = 5.00;

    /**
     * Valor cobrado para os primeiros 60 minutos de estacionamento.
     */
    private static final double PRIMEIROS_60_MINUTES = 9.25;

    /**
     * Valor adicional cobrado para cada 15 minutos após a primeira hora.
     */
    private static final double ADICIONAL_15_MINUTES = 1.75;

    /**
     * Percentual de desconto aplicado quando o cliente atingiu um número específico de utilizações (cada 10 utilizações).
     */
    private static final double DESCONTO_PERCENTUAL = 0.30;

    /**
     * Gera um recibo único com base no timestamp atual, formatado no padrão "yyyyMMdd-HHmmss".
     *
     * @return Uma string que representa o recibo, no formato "yyyyMMdd-HHmmss".
     */
    public static String gerarRecibo() {
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0, 19); // Remove os nanosegundos
        return recibo.replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }

    /**
     * Calcula o custo total do estacionamento com base no tempo entre a entrada e a saída.
     * As regras de cálculo são:
     * - Até 15 minutos: valor fixo.
     * - Até 60 minutos: outro valor fixo.
     * - Após 60 minutos: é adicionado um valor extra para cada 15 minutos adicionais.
     *
     * @param entrada O horário de entrada no estacionamento.
     * @param saida O horário de saída do estacionamento.
     * @return O custo total do estacionamento, arredondado para duas casas decimais.
     */
    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES); // Calcula a diferença de minutos
        double total = 0.0;

        // Cálculo do custo com base no tempo de permanência
        if (minutes <= 15) {
            total = PRIMEIROS_15_MINUTES;
        } else if (minutes <= 60) {
            total = PRIMEIROS_60_MINUTES;
        } else {
            long addicionalMinutes = minutes - 60; // Minutos adicionais após 60 minutos
            Double totalParts = ((double) addicionalMinutes / 15); // Divisão em blocos de 15 minutos

            // Verifica se há minutos adicionais além de blocos completos de 15 minutos
            if (totalParts > totalParts.intValue()) {
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * (totalParts.intValue() + 1));
            } else {
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * totalParts.intValue());
            }
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * Calcula o desconto no valor do estacionamento com base no número de vezes que o cliente utilizou o estacionamento.
     * O desconto de 30% é aplicado a cada 10 utilizações.
     *
     * @param custo O custo total do estacionamento.
     * @param numeroDeVezes O número de vezes que o cliente utilizou o estacionamento.
     * @return O valor do desconto, arredondado para duas casas decimais.
     */
    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {
        BigDecimal desconto = ((numeroDeVezes > 0) && (numeroDeVezes % 10 == 0))
                ? custo.multiply(new BigDecimal(DESCONTO_PERCENTUAL))
                : new BigDecimal(0);
        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}

