/*package br.com.judev.simpletwitter.JWT;

import br.com.judev.simpletwitter.JWT.JwtService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        String token = extractToken(request); // Extrai o token do cabeçalho da requisição
        log.debug("Token extraído do cabeçalho: {}", token);

        if (token == null || !jwtService.isTokenValid(token)) { // Verifica se o token é nulo ou inválido
            log.info("JWT Token está nulo, vazio ou inválido."); // Registra um log informativo
            filterChain.doFilter(request, response); // Continua com a cadeia de filtros
            return; // Retorna
        }

        String email = jwtService.getEmailFromToken(token); // Obtém o email do usuário a partir do token JWT
        log.debug("Email extraído do token: {}", email);

        if (email == null) {
            log.warn("Email de usuário não encontrado no token JWT."); // Registra um log de aviso
            filterChain.doFilter(request, response); // Continua com a cadeia de filtros
            return; // Retorna
        }

        toAuthentication(request, email); // Realiza a autenticação do usuário

        filterChain.doFilter(request, response); // Continua com a cadeia de filtros
    }

    private void toAuthentication(HttpServletRequest request, String email) {
        log.debug("Iniciando autenticação para o email: {}", email);
        UserDetails userDetails = userRepository.findByEmail(email); // Buscar o usuário pelo email
        if (userDetails == null) {
            log.warn("Detalhes do usuário não encontrados para o email: {}", email); // Registra um log de aviso
            return; // Retorna
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()); // Cria um token de autenticação com os detalhes do usuário

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Define os detalhes da autenticação

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Define a autenticação no contexto de segurança
        log.info("Autenticação realizada com sucesso para o email: {}", email);
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
*/