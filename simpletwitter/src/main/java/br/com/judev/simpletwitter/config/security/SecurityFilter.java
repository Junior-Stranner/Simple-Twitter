package br.com.judev.simpletwitter.config.security;

import br.com.judev.simpletwitter.service.TokenService;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityFilter  extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private TokenService tokenService; // Injeção do serviço TokenService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(TokenService.JWT_AUTHORIZATION); // Obtém o cabeçalho Authorization

        String username = null;
        String jwtToken = null;

        // Verifica se o cabeçalho Authorization está presente e no formato correto
        if (authorizationHeader != null && authorizationHeader.startsWith(TokenService.JWT_BEARER)) {
            jwtToken = authorizationHeader.substring(TokenService.JWT_BEARER.length()); // Extrai somente o token JWT
            try {
                username = tokenService.getUsernameFromToken(jwtToken); // Obtém o nome de usuário a partir do token JWT
            } catch (Exception e) {
                log.error("Erro ao extrair nome de usuário do token JWT", e);
            }
        } else {
            log.warn("Token JWT ausente ou não começando com 'Bearer '");
        }

        // Se o nome de usuário foi obtido com sucesso e o contexto de segurança não está definido
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = tokenService.loadUserByUsername(username); // Obtém os detalhes do usuário pelo nome de usuário

            // Se userDetails != null, continue
            if (userDetails != null && tokenService.validateToken(jwtToken)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); // Cria um token de autenticação com os detalhes do usuário

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Define os detalhes da autenticação

                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Define a autenticação no contexto de segurança
            }
        }

        filterChain.doFilter(request, response); // Continua com a cadeia de filtros
    }
}
