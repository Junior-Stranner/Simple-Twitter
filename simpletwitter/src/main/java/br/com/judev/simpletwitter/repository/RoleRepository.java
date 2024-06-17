package br.com.judev.simpletwitter.repository;

import br.com.judev.simpletwitter.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}
