package com.tarmiz.SIRH_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarmiz.SIRH_backend.model.DTO.AuthDTOs.LoginRequestDTO;
import com.tarmiz.SIRH_backend.model.entity.User;
import com.tarmiz.SIRH_backend.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        // Initialize MockMvc and ObjectMapper explicitly
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper();

        userRepository.deleteAll();

        // Create a test user
        User user = new User();
        user.setEmail("admin@example.com");
        user.setPasswordHash(passwordEncoder.encode("admin123"));
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");

        userRepository.save(user);
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@example.com");
        request.setPassword("admin123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@example.com");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testLoginInactiveUser() throws Exception {
        User user = userRepository.findByEmail("admin@example.com").get();
        user.setStatus("INACTIVE");
        userRepository.save(user);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@example.com");
        request.setPassword("admin123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }
}
