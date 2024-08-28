package com.walter.demopark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuração para ativar a auditoria do JPA no Spring.
 *
 * Esta classe é responsável por habilitar a auditoria JPA no contexto do Spring e implementar a interface
 * AuditorAware<String>, que define o auditor (usuário responsável) para ser usado nas anotações de auditoria
 * como @CreatedBy e @LastModifiedBy em entidades JPA. Ela permite que o nome do usuário autenticado seja
 * automaticamente preenchido nos campos de auditoria.
 *
 * Anotações:
 * - @Configuration: Define esta classe como uma classe de configuração no Spring.
 * - @EnableJpaAuditing: Habilita o recurso de auditoria do JPA no contexto do Spring.
 */
@Configuration
@EnableJpaAuditing
public class SpringJpaAuditingConfig implements AuditorAware<String> {

    /**
     * Método responsável por fornecer o auditor atual (usuário autenticado) para ser usado nas operações de
     * auditoria JPA.
     *
     * Este método busca o nome do usuário autenticado no contexto de segurança do Spring. Se o usuário estiver
     * autenticado, retorna seu nome encapsulado em um Optional. Caso contrário, retorna um Optional.empty().
     *
     * @return Optional<String> contendo o nome do usuário autenticado ou Optional.empty() se não houver um
     *         usuário autenticado.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        // Obtém a autenticação do contexto de segurança do Spring
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se há um usuário autenticado
        if (auth != null && auth.isAuthenticated()) {
            // Retorna o nome do usuário autenticado
            return Optional.of(auth.getName());
        }

        // Retorna Optional.empty() se não houver autenticação
        return Optional.empty();
    }
}
