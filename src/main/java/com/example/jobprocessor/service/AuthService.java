package com.example.jobprocessor.service;
import com.example.jobprocessor.dto.LoginRequest;
import com.example.jobprocessor.dto.LoginResponse;
import com.example.jobprocessor.dto.RegisterRequest;
import com.example.jobprocessor.entity.User;
import com.example.jobprocessor.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
         this.jwtService = jwtService;  
    }

    public String register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole("USER");

        userRepository.save(user);

        return "User Registered Successfully";
    }
    public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword())) {

        throw new RuntimeException("Invalid password");
    }

    String token = jwtService.generateToken(user.getUsername());

    return new LoginResponse(token);
}
}