package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for LoginController.
 * Tests authentication and error handling endpoints.
 */
@WebMvcTest(controllers = LoginController.class)
@DisplayName("LoginController Integration Tests")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .username("testuser")
                .password("password")
                .fullname("Test User")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Should display login page")
    void testLoginPage() throws Exception {
        // When & Then - Spring Security gère /login directement
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Please sign in")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("form-signin")));
    }

    @Test
    @DisplayName("Should display secure article details with users list")
    @WithMockUser(roles = "ADMIN")
    void testSecureArticleDetails() throws Exception {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        // When & Then
        mockMvc.perform(get("/secure/article-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should display error page with error message")
    @WithMockUser(roles = "USER")
    void testErrorPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("403"))
                .andExpect(model().attributeExists("errorMsg"))
                .andExpect(model().attribute("errorMsg", "You are not authorized for the requested data."));
    }
}
