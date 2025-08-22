package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController.
 * Tests all endpoints for user management with BCrypt authentication.
 */
@WebMvcTest(controllers = UserController.class)
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
                .password("password123")
                .fullname("Test User")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Should display user list successfully")
    @WithMockUser(roles = "ADMIN")
    void testUserListPage() throws Exception {
        // Given
        when(userService.findAll()).thenReturn(Collections.singletonList(testUser));

        // When & Then
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));

        verify(userService).findAll();
    }

    @Test
    @DisplayName("Should show add user form")
    @WithMockUser(roles = "ADMIN")
    void testShowAddUserForm() throws Exception {
        // When & Then
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("Should validate and save new user successfully")
    @WithMockUser(roles = "ADMIN")
    void testValidateUserSuccess() throws Exception {
        // Given
        when(userService.saveWithPasswordEncoding(any(User.class))).thenReturn(testUser);

        // When & Then - Utiliser des données valides selon les contraintes de validation
        mockMvc.perform(post("/user/validate")
                .with(csrf())
                .param("username", "newuser")
                .param("password", "ValidPassword123!")  // Mot de passe avec majuscule, chiffre et symbole
                .param("fullname", "New User")
                .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).saveWithPasswordEncoding(any(User.class));
    }

    @Test
    @DisplayName("Should handle validation errors when adding a user")
    @WithMockUser(roles = "ADMIN")
    void testValidateUserWithErrors() throws Exception {
        // When & Then - Tester avec des données invalides
        mockMvc.perform(post("/user/validate")
                .with(csrf())
                .param("username", "ab") // Trop court (minimum 3 caractères)
                .param("password", "short") // Ne respecte pas le pattern requis
                .param("fullname", "") // Vide
                .param("role", "INVALID")) // Rôle invalide
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("Should handle duplicate username error")
    @WithMockUser(roles = "ADMIN")
    void testValidateUserDuplicateUsername() throws Exception {
        // Given - Simuler une exception DataIntegrityViolationException
        doThrow(new DataIntegrityViolationException("Duplicate username"))
                .when(userService).saveWithPasswordEncoding(any(User.class));

        // When & Then - Utiliser des données valides pour que la validation passe
        mockMvc.perform(post("/user/validate")
                .with(csrf())
                .param("username", "existinguser")
                .param("password", "ValidPassword123!")  // Mot de passe valide
                .param("fullname", "Existing User")
                .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("errorMessage", "Username already exists. Please choose another username."));
    }

    @Test
    @DisplayName("Should show update form for existing user")
    @WithMockUser(roles = "ADMIN")
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
    @DisplayName("Should redirect when user not found for update")
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser
    void testUpdateUserSuccess() throws Exception {
        // Given
        when(userService.findById(1)).thenReturn(Optional.of(testUser));
        when(userService.findByUsername("updateduser")).thenReturn(Optional.empty());

        // When & Then - Utiliser des données valides
        mockMvc.perform(post("/user/update/1")
                .with(csrf())
                .param("username", "updateduser")
                .param("password", "NewPassword123!")  // Mot de passe valide avec contraintes
                .param("fullname", "Updated User")
                .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).findById(1);
        verify(userService).findByUsername("updateduser");
        verify(userService).updateUserWithNewPassword(any(User.class), anyString());
    }

    @Test
    @DisplayName("Should show validation errors when updating with invalid data")
    @WithMockUser
    void testUpdateUserWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/user/update/1")
                .with(csrf())
                .param("username", "")          // Invalid empty username
                .param("password", "")          // Invalid empty password
                .param("fullname", "")          // Invalid empty fullname
                .param("role", ""))             // Invalid empty role
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));

        verify(userService, never()).updateUserWithNewPassword(any(User.class), anyString());
    }

    @Test
    @DisplayName("Should handle duplicate username during user update")
    @WithMockUser
    void testUpdateUserWithDuplicateUsername() throws Exception {
        // Given
        User existingUserWithSameUsername = User.builder()
                .id(2)
                .username("duplicateuser")
                .build();
        when(userService.findByUsername("duplicateuser")).thenReturn(Optional.of(existingUserWithSameUsername));

        // When & Then - Utiliser des données valides pour que la validation passe
        mockMvc.perform(post("/user/update/1")
                .with(csrf())
                .param("username", "duplicateuser")
                .param("password", "ValidPassword123!")  // Mot de passe valide
                .param("fullname", "Test User")
                .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Username already exists. Please choose another username."));

        verify(userService, never()).updateUserWithNewPassword(any(User.class), anyString());
    }

    @Test
    @DisplayName("Should delete user successfully")
    @WithMockUser
    void testDeleteUser() throws Exception {
        // Given
        when(userService.existsById(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).deleteById(1);
    }

    @Test
    @DisplayName("Should handle delete request for non-existing user")
    @WithMockUser
    void testDeleteNonExistingUser() throws Exception {
        // Given
        when(userService.existsById(99)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/user/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, never()).deleteById(anyInt());
    }
}
