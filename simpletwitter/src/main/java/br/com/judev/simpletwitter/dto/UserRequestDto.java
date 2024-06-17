package br.com.judev.simpletwitter.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class UserRequestDto {

    @NotBlank
    @Email(message = "formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String username;
    @NotBlank
    @Size(min = 6, max = 10)
    private String password;

    public UserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserRequestDto() {
    }

    public @NotBlank @Email(message = "formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Email(message = "formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$") String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 6, max = 10) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 6, max = 10) String password) {
        this.password = password;
    }
}
