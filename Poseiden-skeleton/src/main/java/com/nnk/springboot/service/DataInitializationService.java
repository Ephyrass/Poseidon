package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User data initialization service with BCrypt passwords.
 *
 * This service runs at application startup to create default users
 * with properly encoded passwords.
 */
@Service
public class DataInitializationService implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeDefaultUsers();
    }

    /**
     * Initialize default users if they don't already exist.
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
