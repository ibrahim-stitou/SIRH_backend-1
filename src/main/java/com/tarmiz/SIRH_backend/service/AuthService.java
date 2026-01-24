package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.config.JwtTokenProvider;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.model.DTO.LoginRequestDTO;
import com.tarmiz.SIRH_backend.model.DTO.LoginResponseDTO;
import com.tarmiz.SIRH_backend.model.entity.User;
import com.tarmiz.SIRH_backend.model.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("Invalid email or password");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException("User account is not active");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getRole(), user.getStatus());
        return new LoginResponseDTO(token);
    }
}
