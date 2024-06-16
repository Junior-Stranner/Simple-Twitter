package br.com.judev.simpletwitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF (Cross-Site Request Forgery), geralmente desabilitado em APIs RESTful
                .formLogin(from -> from.disable()) // Desabilita o formulário de login padrão do Spring Security
                .httpBasic(basic -> basic.disable()) // Desabilita autenticação HTTP Basic padrão do Spring Security
                .authorizeHttpRequests(auth -> auth // Desabilita autenticação HTTP Basic padrão do Spring Security
                        .requestMatchers(HttpMethod.POST, "/users").permitAll() // Permite acesso público (sem autenticação) aos endpoints POST /users e POST /login
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyRequest().authenticated() // Requer autenticação para todos os outros endpoints

                        // Configura a política de gerenciamento de sessão como STATELESS (sem estado)
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
         // Retorna o filtro de segurança configurado

    }

}
