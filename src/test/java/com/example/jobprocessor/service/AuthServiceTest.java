package com.example.jobprocessor.service;

import com.example.jobprocessor.dto.LoginRequest;
import com.example.jobprocessor.dto.LoginResponse;
import com.example.jobprocessor.dto.RegisterRequest;
import com.example.jobprocessor.entity.User;
import com.example.jobprocessor.exception.DuplicateResourceException;
import com.example.jobprocessor.exception.InvalidCredentialsException;
import com.example.jobprocessor.exception.ResourceNotFoundException;
import com.example.jobprocessor.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldRegisterUserSuccessfully() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("anushka");
        request.setPassword("password123");

        when(userRepository.findByUsername("anushka"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        String result = authService.register(request);

        assertEquals("User Registered Successfully", result);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowExceptionWhenUsernameAlreadyExists() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("anushka");
        request.setPassword("password123");

        User existingUser = new User();
        existingUser.setUsername("anushka");

        when(userRepository.findByUsername("anushka"))
                .thenReturn(Optional.of(existingUser));

        assertThrows(
                DuplicateResourceException.class,
                () -> authService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_shouldReturnJwtToken() {

        LoginRequest request = new LoginRequest();
        request.setUsername("anushka");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("anushka");
        user.setPassword("encoded-password");
        user.setRole("USER");

        when(userRepository.findByUsername("anushka"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "encoded-password"))
                .thenReturn(true);

        when(jwtService.generateToken("anushka"))
                .thenReturn("jwt-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);

        assertEquals(
                "jwt-token",
                response.getToken()
        );

        verify(jwtService).generateToken("anushka");
    }

    @Test
    void login_shouldThrowUserNotFoundException() {

        LoginRequest request = new LoginRequest();
        request.setUsername("anushka");
        request.setPassword("password123");

        when(userRepository.findByUsername("anushka"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_shouldThrowInvalidPasswordException() {

        LoginRequest request = new LoginRequest();
        request.setUsername("anushka");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setUsername("anushka");
        user.setPassword("encoded-password");

        when(userRepository.findByUsername("anushka"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongpassword",
                "encoded-password"))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never()).generateToken(any());
    }
}