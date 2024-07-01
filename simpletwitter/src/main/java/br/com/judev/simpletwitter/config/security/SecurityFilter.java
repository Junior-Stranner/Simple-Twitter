package br.com.judev.simpletwitter.config.security;

import br.com.judev.simpletwitter.entities.User;
import br.com.judev.simpletwitter.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository; // Injeção de dependência do repositório de usuários

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token JWT presente no cabeçalho de autorização da requisição
        var token = this.recoverToken(request);

        // Valida o token JWT e extrai o login (email) do usuário, se válido
        var login = tokenService.validateToken(token);

        // Se o login (email) extraído do token for válido
        if(login != null){
            // Busca o usuário correspondente ao email no banco de dados
            User user = userRepository.findByUsername(login).orElseThrow(() -> new RuntimeException("User Not Found"));

            // Define as autorizações do usuário (por exemplo, ROLE_USER)
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            // Cria uma instância de autenticação com as informações do usuário
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            // Define a autenticação no contexto de segurança do Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua o encadeamento do filtro para processar a requisição
        filterChain.doFilter(request, response);
    }

    // Método para recuperar o token JWT do cabeçalho de autorização da requisição
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");

        // Se o cabeçalho de autorização estiver ausente, retorna null
        if(authHeader == null) return null;

        // Remove a parte "Bearer " do valor do cabeçalho de autorização e retorna o token JWT
        return authHeader.replace("Bearer ", "");
    }
}