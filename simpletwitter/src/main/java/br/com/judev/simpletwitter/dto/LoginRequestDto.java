package br.com.judev.simpletwitter.dto;

import br.com.judev.simpletwitter.entities.User;

public class LoginRequestDto {

    private String username;
    private String password;

    public LoginRequestDto() {
    }


    public LoginRequestDto(User entity) {
        username = entity.getUsername();
        password = entity.getPassword();
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
