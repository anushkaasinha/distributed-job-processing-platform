package com.example.jobprocessor.dto;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(
            min = 3,
            max = 20,
            message = "Username must be between 3 and 20 characters"
    )
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(
            min = 8,
            max = 30,
            message = "Password must be between 8 and 30 characters"
    )
    private String password;

    public RegisterRequest() {}

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