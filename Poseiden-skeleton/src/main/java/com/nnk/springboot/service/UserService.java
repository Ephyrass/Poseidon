package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for user management with secure BCrypt encoding.
 *
 * This service handles password encoding consistently:
 * - Plain text passwords are automatically encoded
 * - Already encoded passwords are not re-encoded
 * - Clear separation between creation and update
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a user by encoding the password if necessary.
     * This method is used for creating users via the web interface.
     *
     * @param user the user to save with a plain text password
     * @return the saved user
     */
    public User saveWithPasswordEncoding(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Saves a user without encoding the password.
     * This method is used for initialization data that already has encoded passwords.
     *
     * @param user the user to save with an already encoded password
     * @return the saved user
     */
    public User saveWithoutPasswordEncoding(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    /**
     * Updates a user with a new plain text password.
     *
     * @param user the user to update
     * @param newPassword the new plain text password
     * @return the updated user
     */
    public User updateUserWithNewPassword(User user, String newPassword) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    /**
     * Finds a user by their ID.
     */
    public Optional<User> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their username.
     */
    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves all users.
     */
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by their ID.
     */
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        userRepository.deleteById(id);
    }

    /**
     * Checks if a user exists with the given ID.
     */
    public boolean existsById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return userRepository.existsById(id);
    }

    /**
     * Checks if a username already exists.
     */
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return userRepository.findByUsername(username).isPresent();
    }
}
