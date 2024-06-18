package br.com.judev.simpletwitter.service;

import br.com.judev.simpletwitter.dto.UserRequestDto;
import br.com.judev.simpletwitter.dto.UserResponseDto;
import br.com.judev.simpletwitter.entities.Role;
import br.com.judev.simpletwitter.entities.User;
import br.com.judev.simpletwitter.repository.RoleRepository;
import br.com.judev.simpletwitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {
        // Verificar se o usuário já existe
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Usuário já existe");
        }

        Optional<Role> optionalBasicRole = (Optional<Role>) roleRepository.findByName(Role.Values.BASIC.name());

        Role basicRole = optionalBasicRole.orElseThrow(() -> new RuntimeException("Role básica não encontrada"));

        // Criar novo usuário
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(basicRole));

        // Salvar no banco de dados
        userRepository.save(user);

        // Criar e retornar o DTO de resposta
        return new UserResponseDto(user.getUserId(), user.getUsername(), user.getRoles());
    }
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
