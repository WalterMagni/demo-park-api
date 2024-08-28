package com.walter.demopark.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * Classe utilitária para manipulação de tokens JWT (JSON Web Token).
 * Essa classe fornece métodos para gerar, validar e extrair informações de tokens JWT
 * utilizando a chave secreta definida e o algoritmo de assinatura HMAC-SHA256 (HS256).
 */

@Slf4j
public class JwtUtils {

    // Prefixo usado nos tokens JWT no cabeçalho "Authorization"
    public static final String JWT_BEARER = "Bearer ";

    // Nome do cabeçalho HTTP onde o token JWT será transmitido
    public static final String JWT_AUTHORIZATION = "Authorization";

    // Chave secreta usada para assinar o token JWT (deve ser mantida em segredo)
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";

    // Definição da duração do token em dias, horas e minutos
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 30;

    // Construtor privado para evitar a instanciação da classe utilitária
    private JwtUtils() {
    }

    /**
     * Gera uma chave HMAC-SHA256 para assinar o token JWT usando a chave secreta.
     * A chave é derivada da string de chave secreta codificada em UTF-8.
     *
     * @return Instância de Key gerada a partir da chave secreta.
     */
    private static Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gera a data de expiração do token JWT com base na data de emissão e nas constantes de expiração.
     *
     * @param start Data de emissão do token.
     * @return Data de expiração calculada.
     */
    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Cria um token JWT assinado com o nome de usuário e o papel (role) fornecidos.
     * O token é assinado usando o algoritmo HMAC-SHA256 e inclui as informações do usuário.
     *
     * @param username Nome de usuário que será incluído no token como o "subject".
     * @param role Papel do usuário, que será incluído como uma "claim" no token.
     * @return Um objeto JwtToken que contém o token JWT gerado.
     */
    public static JwtToken createToken(String username, String role) {
        // Define a data de emissão do token
        Date issuedAt = new Date();
        // Define a data de expiração do token
        Date limit = toExpireDate(issuedAt);

        // Gera o token JWT com as informações fornecidas e a chave secreta
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")  // Define o tipo de token como JWT
                .setSubject(username)          // Define o "subject" como o nome de usuário
                .setIssuedAt(issuedAt)         // Define a data de emissão
                .setExpiration(limit)          // Define a data de expiração
                .signWith(generateKey(), SignatureAlgorithm.HS256)  // Assina o token com HMAC-SHA256
                .claim("role", role)           // Adiciona o papel do usuário como uma "claim"
                .compact();

        // Retorna o token JWT gerado
        return new JwtToken(token);
    }

    /**
     * Extrai as "claims" contidas no token JWT.
     * O token JWT é validado e suas claims são retornadas, ou null em caso de erro.
     *
     * @param token O token JWT a ser processado.
     * @return As claims contidas no token JWT, ou null se o token for inválido.
     */
    private static Claims getClaimsFromToken(String token) {
        try {
            // Faz o parse do token JWT e retorna as claims
            return Jwts.parser()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();
        } catch (JwtException ex) {
            // Loga o erro em caso de token inválido
            log.error(String.format("Token inválido %s", ex.getMessage()));
        }
        return null;
    }

    /**
     * Extrai o nome de usuário do token JWT.
     * O "subject" do token, que corresponde ao nome de usuário, é retornado.
     *
     * @param token O token JWT de onde o nome de usuário será extraído.
     * @return O nome de usuário contido no token JWT.
     */
    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Verifica se o token JWT é válido.
     * O método valida o token, verificando sua assinatura e se ele não está expirado.
     *
     * @param token O token JWT a ser validado.
     * @return true se o token for válido, false caso contrário.
     */
    public static boolean isTokenValid(String token) {
        try {
            // Faz o parse do token JWT para validar sua assinatura e expiração
            Jwts.parser()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token));
            return true;  // Se a validação for bem-sucedida, o token é válido
        } catch (JwtException ex) {
            // Loga o erro em caso de token inválido
            log.error(String.format("Token inválido %s", ex.getMessage()));
        }
        return false;  // Retorna false se o token for inválido ou expirado
    }

    /**
     * Remove o prefixo "Bearer " do token JWT, caso esteja presente.
     * Isso garante que o token processado seja apenas a parte relevante, sem o prefixo.
     *
     * @param token O token JWT que pode conter o prefixo "Bearer ".
     * @return O token JWT sem o prefixo "Bearer ".
     */
    private static String refactorToken(String token) {
        // Se o token contiver o prefixo "Bearer ", remove-o
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        // Retorna o token original se o prefixo "Bearer " não estiver presente
        return token;
    }

}
