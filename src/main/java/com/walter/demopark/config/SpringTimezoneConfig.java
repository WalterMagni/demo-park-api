package com.walter.demopark.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Configuração de fuso horário global da aplicação Spring.
 *
 * Esta classe define o fuso horário padrão para a aplicação Spring como "America/Sao_Paulo".
 * A configuração é aplicada logo após a construção da classe, utilizando o método anotado com @PostConstruct.
 * Isso garante que todas as operações relacionadas a data e hora na aplicação seguirão o fuso horário especificado.
 */
@Configuration
public class SpringTimezoneConfig {

    /**
     * Configura o fuso horário padrão para "America/Sao_Paulo".
     *
     * Este método é executado logo após a inicialização do contexto Spring, devido à anotação @PostConstruct.
     * Ele altera o fuso horário padrão da JVM para o fuso horário de São Paulo, garantindo que toda a aplicação
     * opere com este fuso horário, independentemente do ambiente onde ela está sendo executada.
     */
    @PostConstruct
    public void timezoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));  // Define o fuso horário padrão para São Paulo
    }

}
