package com.example.jobprocessor.controller;

import com.example.jobprocessor.entity.User;
import com.example.jobprocessor.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void register_shouldRegisterUserSuccessfully() throws Exception {

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username":"anushka",
                          "password":"password123"
                        }
                        """)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully"));
    }

    @Test
    void login_shouldReturnJwtToken() throws Exception {

        User user = new User();
        user.setUsername("anushka");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole("USER");

        userRepository.save(user);

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username":"anushka",
                          "password":"password123"
                        }
                        """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}