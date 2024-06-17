package br.com.judev.simpletwitter.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    // Constantes para configuração do JWT
    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789"; // Chave secreta para assinatura do JWT
    public static final long EXPIRE_DAYS = 0; // Dias de expiração do token
    public static final long EXPIRE_HOURS = 0; // Horas de expiração do token
    public static final long EXPIRE_MINUTES = 30; // Minutos de expiração do token

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public JwtService() {
    }

    // Método para gerar a chave de assinatura
    private static SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Método para calcular a data de expiração com base na data de emissão do token
    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Método para gerar um token JWT
    public static JwtToken generateToken(String username, String role) {
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

        return new JwtToken(token); // Retorna o token JWT encapsulado em um objeto JwtToken
    }

    // Método para extrair as reivindicações (claims) do token JWT
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();
        } catch (JwtException ex) {
            log.error(String.format("Token inválido: %s", ex.getMessage()));
        }
        return null;
    }

    // Método para obter o nome de usuário a partir do token JWT
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    // Método para verificar se um token JWT é válido
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error(String.format("Token inválido: %s", ex.getMessage())); // Log error message if token validation fails
        }
        return false;
    }

    // Remove o prefixo "Bearer " do token antes de fazer o parsing
    private static String refactorToken(String token) {
        if (token.startsWith(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }
}
