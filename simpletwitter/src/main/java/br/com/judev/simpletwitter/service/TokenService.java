package br.com.judev.simpletwitter.service;

import br.com.judev.simpletwitter.entities.User;
import br.com.judev.simpletwitter.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {

    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Value("${jwt.secret.key}")
    private String secret;


    private final SecretKey secretKey;



    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 2;

    public TokenService(UserRepository userRepository, SecretKey secretKey) {
        this.userRepository = userRepository;
        this.secretKey = secretKey;
    }


    public String generateToken(User user) {
        try {
            SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
            String token = Jwts.builder()
                    .setIssuer("auth")
                    .setSubject(user.getUsername())
                    .setExpiration(toExpireDateTime())
                    .signWith(generateKey(), algorithm)
                    .compact();
            return token;
        } catch (JwtException exception) {
            log.error("Erro ao gerar o token JWT", exception);
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        } catch (JwtException exception) {
            log.error("Token inválido", exception);
            return false;
        }
    }
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        return (UserDetails) user;
    }

    private SecretKey generateKey() {
        byte[] decodedSecret = secret.getBytes(StandardCharsets.UTF_8);
        return new javax.crypto.spec.SecretKeySpec(decodedSecret, SignatureAlgorithm.HS256.getJcaName());
    }

    private Date toExpireDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime end = currentDateTime.plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    private String refactorToken(String token) {
        if (token.startsWith(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length()); // Remove o prefixo "Bearer "
        }
        return token;
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(refactorToken(token))
                    .getBody();
            return claims.getSubject();
        } catch (JwtException exception) {
            log.error("Falha ao obter o nome de usuário do token", exception);
            throw new RuntimeException("Falha ao obter o nome de usuário do token", exception);
        }
    }
}
