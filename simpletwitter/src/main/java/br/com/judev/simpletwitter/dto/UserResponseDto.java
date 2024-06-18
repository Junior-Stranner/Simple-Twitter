package br.com.judev.simpletwitter.dto;

import br.com.judev.simpletwitter.entities.Role;

import java.util.Set;
import java.util.UUID;

public class UserResponseDto {
    private UUID userId;
    private String username;
    private String password;

    public UserResponseDto(UUID userId, String username, Set<Role> roles) {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
