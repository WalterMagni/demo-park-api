package com.walter.demopark.config;

import com.walter.demopark.jwt.JwtAuthenticationEntryPoint;
import com.walter.demopark.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Classe de configuração de segurança da aplicação usando Spring Security.
 * Essa classe define a cadeia de filtros de segurança e configurações, incluindo a desativação de CSRF,
 * login básico e o uso de um filtro JWT para autenticação de requisições.
 */

@Configuration
@EnableWebMvc
@EnableMethodSecurity
public class SpringSecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança para a aplicação.
     * Esta configuração desativa a proteção CSRF, desativa o login por formulário e autenticação HTTP Basic,
     * permite acesso público a determinados endpoints (como `/api/v1/usuarios` e `/api/v1/auth`),
     * e exige autenticação para todas as outras requisições.
     * Além disso, define a política de sessão como stateless (sem gerenciamento de sessão) e adiciona um filtro JWT
     * antes do filtro de autenticação padrão do Spring Security.
     *
     * @param http Instância do objeto HttpSecurity que permite a customização das configurações de segurança HTTP.
     * @return Uma instância configurada de SecurityFilterChain.
     * @throws Exception Em caso de falha na configuração do filtro de segurança.
     */


    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/docs-park.html", "/docs-park/**",
            "/v3/api-docs/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // Desabilita proteção CSRF (geralmente desabilitado em APIs stateless)
                .csrf(csrf -> csrf.disable())

                // Desabilita o formulário de login padrão do Spring Security
                .formLogin(form -> form.disable())

                // Desabilita a autenticação básica HTTP
                .httpBasic(basic -> basic.disable())

                // Configura permissões de requisições HTTP
                .authorizeHttpRequests(auth ->
                        // Permite POST sem autenticação para criação de usuários e autenticação de login
                        auth.requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                                .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
                                // Exige autenticação para todas as outras requisições
                                .anyRequest().authenticated())

                // Define a política de gerenciamento de sessões como stateless (não há armazenamento de sessões)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Adiciona um filtro de autorização JWT antes do filtro de autenticação padrão
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                // Adiciona o filtro de autenticação padrãos
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                // Constrói e retorna a cadeia de filtros configurada
                .build();
    }

    /**
     * Bean que cria e retorna o filtro de autorização JWT.
     * Esse filtro será responsável por interceptar requisições e validar o token JWT para autenticação.
     *
     * @return Uma instância de JwtAuthorizationFilter.
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    /**
     * Bean que cria e retorna o codificador de senhas baseado no algoritmo BCrypt.
     * O BCrypt é um dos algoritmos mais recomendados para armazenar senhas devido à sua robustez e resistência a ataques de força bruta.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que cria e retorna o gerenciador de autenticação do Spring Security.
     * Este gerenciador de autenticação é responsável por autenticar os usuários no sistema.
     *
     * @param authenticationConfiguration Configuração de autenticação do Spring.
     * @return Uma instância de AuthenticationManager.
     * @throws Exception Se houver falha na obtenção do AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
