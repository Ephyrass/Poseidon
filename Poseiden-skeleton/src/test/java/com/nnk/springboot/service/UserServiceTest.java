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
 * Unit tests for UserService.
 * Verifies all CRUD operations and security features with the new methods.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
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
    @DisplayName("Save with password encoding - Success")
    void saveWithPasswordEncoding_WhenUserValid_ShouldSaveUserWithEncodedPassword() {
        // Given
        String rawPassword = "Password123!";
        String encodedPassword = "$2a$10$encodedPassword";
        testUser.setPassword(rawPassword);

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User savedUser = userService.saveWithPasswordEncoding(testUser);

        // Then
        assertNotNull(savedUser);
        assertEquals(testUser.getUsername(), savedUser.getUsername());
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Save with password encoding - Null user")
    void saveWithPasswordEncoding_WhenUserIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.saveWithPasswordEncoding(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Save without password encoding - Success")
    void saveWithoutPasswordEncoding_WhenUserValid_ShouldSaveUserDirectly() {
        // Given
        String alreadyEncodedPassword = "$2a$10$alreadyEncodedPassword";
        testUser.setPassword(alreadyEncodedPassword);

        when(userRepository.save(testUser)).thenReturn(testUser);

        // When
        User savedUser = userService.saveWithoutPasswordEncoding(testUser);

        // Then
        assertNotNull(savedUser);
        assertEquals(testUser.getUsername(), savedUser.getUsername());
        assertEquals(alreadyEncodedPassword, testUser.getPassword());
        verify(userRepository).save(testUser);
        verifyNoInteractions(passwordEncoder); // Verify that encoder is not called
    }

    @Test
    @DisplayName("Save without password encoding - Null user")
    void saveWithoutPasswordEncoding_WhenUserIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.saveWithoutPasswordEncoding(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Update user password - Success")
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
    @DisplayName("Update user password - Null user")
    void updateUserWithNewPassword_WhenUserIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserWithNewPassword(null, "password")
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Update user password - Empty password")
    void updateUserWithNewPassword_WhenPasswordEmpty_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserWithNewPassword(testUser, "")
        );
        assertEquals("New password cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Find by ID - User found")
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
    @DisplayName("Find by ID - User not found")
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
    @DisplayName("Find by ID - Null ID")
    void findById_WhenIdIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findById(null)
        );
        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Find by username - User found")
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
    @DisplayName("Find by username - Empty username")
    void findByUsername_WhenUsernameEmpty_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findByUsername("")
        );
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Find all users")
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
    @DisplayName("Delete by ID - Success")
    void deleteById_WhenIdValid_ShouldDeleteUser() {
        // Given
        Integer userId = 1;

        // When
        userService.deleteById(userId);

        // Then
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Delete by ID - Null ID")
    void deleteById_WhenIdIsNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteById(null)
        );
        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Check existence by ID - User exists")
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
    @DisplayName("Check existence by ID - User does not exist")
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
    @DisplayName("Check existence by username - User exists")
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
    @DisplayName("Check existence by username - User does not exist")
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
