package br.com.judev.simpletwitter.repository;

import br.com.judev.simpletwitter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
