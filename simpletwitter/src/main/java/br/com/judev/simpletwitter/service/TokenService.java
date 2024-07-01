package br.com.judev.simpletwitter.service;

import br.com.judev.simpletwitter.dto.LoginRequestDto;
import br.com.judev.simpletwitter.dto.LoginResponseDto;
import br.com.judev.simpletwitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;

@Service
public class TokenService {

    private final SecretKey secretKey;
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.expiresInSeconds}")
    private Long expiresIn;

    public TokenService(SecretKey secretKey, JwtEncoder jwtEncoder,
                        UserRepository userRepository,
                        BCryptPasswordEncoder passwordEncoder) {
        this.secretKey = secretKey;
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto generateToken(LoginRequestDto loginRequestDto) {
        var user = userRepository.findByUsername(loginRequestDto.getUsername());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequestDto, passwordEncoder)) {
            throw new BadCredentialsException("User or password is invalid!");
        }

        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDto(jwtValue, expiresIn);
    }

}
