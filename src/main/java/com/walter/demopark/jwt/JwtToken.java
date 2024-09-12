package com.walter.demopark.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe JwtToken que representa um objeto de token JWT.
 * Essa classe é simples e contém apenas um campo: 'token', que armazena o valor do token JWT.
 *
 * As anotações do Lombok (@NoArgsConstructor, @AllArgsConstructor, @Getter, @Setter)
 * são usadas para gerar automaticamente o construtor sem argumentos, o construtor com todos os argumentos,
 * bem como os métodos getter e setter para os campos da classe.
 */
@NoArgsConstructor  // Gera automaticamente um construtor sem argumentos
@AllArgsConstructor // Gera automaticamente um construtor com todos os argumentos
@Getter             // Gera automaticamente os métodos getter para todos os campos da classe
@Setter             // Gera automaticamente os métodos setter para todos os campos da classe
public class JwtToken {

    /**
     * O campo 'token' armazena o valor do token JWT.
     */
    private String token;
}
