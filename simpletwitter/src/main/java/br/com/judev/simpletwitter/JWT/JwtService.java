package br.com.judev.simpletwitter.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${api.security.token.secret}")
    private static String secret;

    /**
     * Construtor JwtService. Verifica se a chave secreta JWT foi configurada corretamente.
     * Se não estiver configurada, lança uma exceção IllegalStateException.
     */
    public JwtService() {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("A chave secreta JWT não foi configurada corretamente nas variáveis de ambiente.");
        }
    }

    /**
     * Método para gerar a chave de assinatura usando a chave secreta configurada.
     *
     * @return SecretKey - Chave de assinatura HMAC-SHA para JWT.
     */
    private static SecretKey generateKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Método para gerar um token JWT com base no nome de usuário e papel (role).
     *
     * @param username - Nome de usuário para o qual o token será gerado.
     * @param role     - Papel (role) associado ao usuário.
     * @return JwtToken - Objeto JwtToken encapsulando o token JWT gerado.
     */
    public String generateToken(String username, String role) {
        Date issuedAt = new Date(); // Data de emissão do token
        Date expiration = toExpireDate(issuedAt); // Data de expiração do token

        // Construção do token JWT
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .claim("role", role)
                .compact();

        return token;// Retorna o token JWT encapsulado em um objeto JwtToken
    }

    /**
     * Método para calcular a data de expiração com base na data de emissão do token.
     *
     * @param start - Data de emissão do token.
     * @return Date - Data de expiração calculada.
     */
    private Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(0).plusHours(0).plusMinutes(30); // Expiração em 30 minutos
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Método para extrair as reivindicações (claims) do token JWT.
     *
     * @param token - Token JWT do qual as reivindicações serão extraídas.
     * @return Claims - Objeto Claims contendo as reivindicações do token JWT.
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(refactorToken(token))
                    .getBody();
        } catch (JwtException ex) {
            log.error(String.format("Token inválido: %s", ex.getMessage()));
            return null;
        }
    }

    /**
     * Método para obter o nome de usuário a partir do token JWT.
     *
     * @param token - Token JWT do qual o nome de usuário será extraído.
     * @return String - Nome de usuário contido no token JWT, ou null se o token for inválido.
     */
    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * Método para verificar se um token JWT é válido.
     *
     * @param token - Token JWT a ser validado.
     * @return boolean - True se o token for válido, False se o token for inválido.
     */
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error(String.format("Token inválido: %s", ex.getMessage()));
            return false;
        }
    }

    /**
     * Remove o prefixo "Bearer " do token antes de fazer o parsing.
     *
     * @param token - Token JWT que pode conter o prefixo "Bearer ".
     * @return String - Token JWT sem o prefixo "Bearer ".
     */
    private static String refactorToken(String token) {
        if (token.startsWith(JWT_BEARER)) {
            return token.substring(7);
        }
        return token;
    }
}
