package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour UserService.
 * Vérifie toutes les opérations CRUD et les fonctionnalités de sécurité.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
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
    @DisplayName("Sauvegarder un utilisateur - Succès")
    void save_WhenUserValid_ShouldSaveUserWithEncodedPassword() {
        // Given
        String rawPassword = "Password123!";
        String encodedPassword = "$2a$10$encodedPassword";
        testUser.setPassword(rawPassword);

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User savedUser = userService.save(testUser);

        // Then
        assertNotNull(savedUser);
        assertEquals(testUser.getUsername(), savedUser.getUsername());
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Sauvegarder un utilisateur - Utilisateur null")
    void save_WhenUserIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.save(null)
        );
        assertEquals("L'utilisateur ne peut pas être null", exception.getMessage());
    }

    @Test
    @DisplayName("Mettre à jour le mot de passe - Succès")
    void updateUserWithNewPassword_WhenValid_ShouldUpdatePassword() {
        // Given
        String newPassword = "NewPassword123!";
        String encodedPassword = "$2a$10$newEncodedPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(testUser)).thenReturn(testUser);

        // When
        User updatedUser = userService.updateUserWithNewPassword(testUser, newPassword);

        // Then
        assertNotNull(updatedUser);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Mettre à jour le mot de passe - Utilisateur null")
    void updateUserWithNewPassword_WhenUserIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserWithNewPassword(null, "password")
        );
        assertEquals("L'utilisateur ne peut pas être null", exception.getMessage());
    }

    @Test
    @DisplayName("Mettre à jour le mot de passe - Mot de passe vide")
    void updateUserWithNewPassword_WhenPasswordEmpty_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserWithNewPassword(testUser, "")
        );
        assertEquals("Le nouveau mot de passe ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Trouver par ID - Utilisateur trouvé")
    void findById_WhenUserExists_ShouldReturnUser() {
        // Given
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findById(userId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Trouver par ID - Utilisateur non trouvé")
    void findById_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        Integer userId = 99;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findById(userId);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Trouver par ID - ID null")
    void findById_WhenIdIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findById(null)
        );
        assertEquals("L'identifiant ne peut pas être null", exception.getMessage());
    }

    @Test
    @DisplayName("Trouver par nom d'utilisateur - Utilisateur trouvé")
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        // Given
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByUsername(username);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Trouver par nom d'utilisateur - Nom vide")
    void findByUsername_WhenUsernameEmpty_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findByUsername("")
        );
        assertEquals("Le nom d'utilisateur ne peut pas être vide", exception.getMessage());
    }

    @Test
    @DisplayName("Trouver tous les utilisateurs")
    void findAll_ShouldReturnAllUsers() {
        // Given
        User user2 = User.builder()
                .id(2)
                .username("user2")
                .password("password")
                .fullname("User Two")
                .role("ADMIN")
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        // When
        Iterable<User> result = userService.findAll();

        // Then
        assertNotNull(result);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Supprimer par ID - Succès")
    void deleteById_WhenIdValid_ShouldDeleteUser() {
        // Given
        Integer userId = 1;

        // When
        userService.deleteById(userId);

        // Then
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Supprimer par ID - ID null")
    void deleteById_WhenIdIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteById(null)
        );
        assertEquals("L'identifiant ne peut pas être null", exception.getMessage());
    }

    @Test
    @DisplayName("Vérifier existence par ID - Utilisateur existe")
    void existsById_WhenUserExists_ShouldReturnTrue() {
        // Given
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(true);

        // When
        boolean result = userService.existsById(userId);

        // Then
        assertTrue(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    @DisplayName("Vérifier existence par ID - Utilisateur n'existe pas")
    void existsById_WhenUserNotExists_ShouldReturnFalse() {
        // Given
        Integer userId = 99;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When
        boolean result = userService.existsById(userId);

        // Then
        assertFalse(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    @DisplayName("Vérifier existence par nom d'utilisateur - Utilisateur existe")
    void existsByUsername_WhenUserExists_ShouldReturnTrue() {
        // Given
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        boolean result = userService.existsByUsername(username);

        // Then
        assertTrue(result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Vérifier existence par nom d'utilisateur - Utilisateur n'existe pas")
    void existsByUsername_WhenUserNotExists_ShouldReturnFalse() {
        // Given
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        boolean result = userService.existsByUsername(username);

        // Then
        assertFalse(result);
        verify(userRepository).findByUsername(username);
    }
}
