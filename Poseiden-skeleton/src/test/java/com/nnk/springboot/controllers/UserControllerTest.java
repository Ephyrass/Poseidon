package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController.
 * Tests all endpoints with proper validation.
 */
@WebMvcTest(UserController.class)
@DisplayName("UserController Integration Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .username("testuser")
                .password("Password123!")
                .fullname("Test User")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Should display user list")
    void testUserList() throws Exception {
        // Given
        when(userService.findAll()).thenReturn(Arrays.asList(testUser));

        // When & Then
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));

        verify(userService).findAll();
    }

    @Test
    @DisplayName("Should show add user form")
    void testShowAddUserForm() throws Exception {
        // When & Then
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("Should validate and save new user successfully")
    void testValidateUserSuccess() throws Exception {
        // Given
        when(userService.saveWithPasswordEncoding(any(User.class))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(post("/user/validate")
                .param("username", "newuser")
                .param("password", "Password123!")
                .param("fullname", "New User")
                .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).saveWithPasswordEncoding(any(User.class));
    }

    @Test
    @DisplayName("Should show validation errors for invalid user data")
    void testValidateUserWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/user/validate")
                .param("username", "") // Invalid empty username
                .param("password", "123") // Invalid weak password
                .param("fullname", "")
                .param("role", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors());

        verify(userService, never()).saveWithPasswordEncoding(any(User.class));
    }

    @Test
    @DisplayName("Should show update form for existing user")
    void testShowUpdateForm() throws Exception {
        // Given
        when(userService.findById(1)).thenReturn(Optional.of(testUser));

        // When & Then
        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));

        verify(userService).findById(1);
    }

    @Test
    @DisplayName("Should redirect to list when user not found for update")
    void testShowUpdateFormUserNotFound() throws Exception {
        // Given
        when(userService.findById(99)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/user/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).findById(99);
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccess() throws Exception {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userService.findById(1)).thenReturn(Optional.of(testUser));

        // When & Then
        mockMvc.perform(post("/user/update/1")
                .param("username", "testuser")
                .param("password", "NewPassword123!")
                .param("fullname", "Updated User")
                .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).updateUserWithNewPassword(any(User.class), eq("NewPassword123!"));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() throws Exception {
        // Given
        when(userService.existsById(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).existsById(1);
        verify(userService).deleteById(1);
    }

    @Test
    @DisplayName("Should handle delete request for non-existing user")
    void testDeleteNonExistingUser() throws Exception {
        // Given
        when(userService.existsById(99)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/user/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).existsById(99);
        verify(userService, never()).deleteById(anyInt());
    }
}
