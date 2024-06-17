/*package br.com.judev.simpletwitter.config;

import br.com.judev.simpletwitter.entities.Role;
import br.com.judev.simpletwitter.entities.User;
import br.com.judev.simpletwitter.repository.RoleRepository;
import br.com.judev.simpletwitter.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserConfig.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Injetando a senha do administrador através de uma variável de ambiente
    @Value("${admin.password}")
    private String adminPassword;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Busca a role ADMIN no banco de dados
        Role roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        // Busca o usuário admin no banco de dados
        Optional<User> userAdmin = userRepository.findByUsername("admin");

        // Verifica se o usuário admin já existe
        userAdmin.ifPresentOrElse(
                user -> {
                    logger.info("Admin já existe");
                },
                () -> {
                    // Caso o usuário admin não exista, cria um novo usuário admin
                    User user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode(adminPassword)); // Define a senha codificada
                    user.setRoles(Set.of(roleAdmin)); // Define a role ADMIN para o usuário
                    userRepository.save(user); // Salva o novo usuário no banco de dados
                    logger.info("Usuário admin criado com sucesso");
                }
        );
    }
}*/
