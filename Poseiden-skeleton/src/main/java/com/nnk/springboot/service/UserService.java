package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for user management with secure BCrypt encoding.
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
     * Save a user and ensure the user's password is encoded using the configured
     * PasswordEncoder if a non-empty plain text password is present.
     * This method is intended for use when creating or updating users from the
     * web interface where passwords are provided in plain text.
     *
     * @param user the user to save; must not be null
     * @return the saved User instance as returned by the repository
     * @throws IllegalArgumentException if the provided user is null
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
     * Save a user without modifying or encoding the password. Use this method
     * for initialization or import scenarios where the password is already
     * encoded and must be preserved.
     *
     * @param user the user to save; must not be null
     * @return the saved User instance as returned by the repository
     * @throws IllegalArgumentException if the provided user is null
     */
    public User saveWithoutPasswordEncoding(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    /**
     * Update the provided user with a new plain text password. The password
     * will be encoded before persisting the change.
     *
     * @param user the user to update; must not be null
     * @param newPassword the new plain text password; must not be null or empty
     * @return the updated User instance as returned by the repository
     * @throws IllegalArgumentException if the user is null or the newPassword is empty
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
     * Find a user by its database identifier.
     *
     * @param id the identifier of the user to find; must not be null
     * @return an Optional containing the User if found, or empty if not found
     * @throws IllegalArgumentException if id is null
     */
    public Optional<User> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return userRepository.findById(id);
    }

    /**
     * Find a user by username.
     *
     * @param username the username to search for; must not be null or empty
     * @return an Optional containing the User if found, or empty if not found
     * @throws IllegalArgumentException if username is null or empty
     */
    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieve all users from the repository.
     *
     * @return an Iterable of all User instances
     */
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Delete a user by its database identifier.
     *
     * @param id the identifier of the user to delete; must not be null
     * @throws IllegalArgumentException if id is null
     */
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        userRepository.deleteById(id);
    }

    /**
     * Check whether a user exists with the provided identifier.
     *
     * @param id the identifier to check; must not be null
     * @return true if a user exists with the given id, false otherwise
     * @throws IllegalArgumentException if id is null
     */
    public boolean existsById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return userRepository.existsById(id);
    }

    /**
     * Check whether a username already exists in the system.
     *
     * @param username the username to check; must not be null or empty
     * @return true if a user with the given username exists, false otherwise
     * @throws IllegalArgumentException if username is null or empty
     */
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return userRepository.findByUsername(username).isPresent();
    }
}
