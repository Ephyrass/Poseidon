package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User data initialization service with BCrypt passwords.
 * <p>Runs at application startup (only when 'dev' profile is active) and creates
 * default users with encoded passwords for development and testing.
 */
@Service
@Profile("dev")
public class DataInitializationService implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Triggers initialization of default users required for local development and testing.
     * Entry point executed at application startup when the 'dev' profile is active.
     * This method triggers initialization of default users required for local
     * development and testing. It delegates the actual creation logic to
     * initializeDefaultUsers().
     * @param args startup arguments passed by the Spring Boot runtime
     * @throws Exception propagated exceptions from initialization routines
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeDefaultUsers();
    }

    /**
     * Create default users (admin and standard user) if they do not already
     * exist in the database. Passwords provided here are plain text and will
     * be encoded by UserService.saveWithPasswordEncoding() before persisting.
     *
     * This method is private and intended to be invoked during startup only.
     */
    private void initializeDefaultUsers() {
        // Create admin user if it doesn't exist
        if (!userService.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password("Password123!") // Will be encoded by saveWithPasswordEncoding
                    .fullname("Administrator")
                    .role("ADMIN")
                    .build();
            userService.saveWithPasswordEncoding(admin);
            System.out.println("✅ Admin user created successfully");
        }

        // Create standard user if it doesn't exist
        if (!userService.existsByUsername("user")) {
            User user = User.builder()
                    .username("user")
                    .password("Password123!") // Will be encoded by saveWithPasswordEncoding
                    .fullname("Standard User")
                    .role("USER")
                    .build();
            userService.saveWithPasswordEncoding(user);
            System.out.println("✅ Standard user created successfully");
        }
    }
}
