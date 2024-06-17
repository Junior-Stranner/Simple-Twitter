package br.com.judev.simpletwitter.dto;

public class UserResponseDto {
    private long userId;
    private String username;
    private String password;

    public UserResponseDto(String username, String password, long userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public UserResponseDto() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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
