package br.com.judev.simpletwitter.JWT;

import br.com.judev.simpletwitter.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(JwtService.JWT_AUTHORIZATION); // Obtém o token JWT do cabeçalho da requisição

        if (token == null || !token.startsWith(JwtService.JWT_BEARER)) { // Verifica se o token está ausente ou não começa com 'Bearer '
            log.info("JWT Token está nulo, vazio ou não iniciado com 'Bearer '."); // Registra um log informativo
            filterChain.doFilter(request, response); // Continua com a cadeia de filtros
            return; // Retorna
        }

        if (!JwtService.isTokenValid(token)) { // Verifica se o token JWT é inválido ou expirou
            log.warn("JWT Token está inválido ou expirado."); // Registra um log de aviso
            filterChain.doFilter(request, response); // Continua com a cadeia de filtros
            return; // Retorna
        }
        var subject = jwtService.getEmailFromToken(token);

        if (subject == null) {
            log.warn("email de usuário não encontrado no token JWT.");
            filterChain.doFilter(request, response); // Continua com a cadeia de filtros
            return;
        }

        UserDetails user = userRepository.findByEmail(subject); // Buscar o usuário pelo email
        if (user == null) {
            log.warn("Detalhes do usuário não encontrados para o email: {}", subject);
            filterChain.doFilter(request, response); // Continua com a cadeia de filtros
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response); // Continua com a cadeia de filtros
    }

    // Método para extrair o token JWT do cabeçalho da requisição
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtService.JWT_AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(JwtService.JWT_BEARER)) {
            return authHeader.substring(JwtService.JWT_BEARER.length()).trim();
        }
        return null;
    }

}
