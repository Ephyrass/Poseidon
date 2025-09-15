package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataInitializationServiceTest {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private DataInitializationService dataInitializationService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        dataInitializationService = new DataInitializationService(userService, passwordEncoder);
    }

    @Test
    void run_shouldCreateDefaultUsers_whenTheyDoNotExist() throws Exception {
        when(userService.existsByUsername("admin")).thenReturn(false);
        when(userService.existsByUsername("user")).thenReturn(false);

        dataInitializationService.run();

        // verify that saveWithPasswordEncoding was called for admin and user
        verify(userService, times(2)).saveWithPasswordEncoding(any(User.class));
    }

    @Test
    void run_shouldNotCreateUsers_whenTheyAlreadyExist() throws Exception {
        when(userService.existsByUsername("admin")).thenReturn(true);
        when(userService.existsByUsername("user")).thenReturn(true);

        dataInitializationService.run();

        verify(userService, never()).saveWithPasswordEncoding(any(User.class));
    }
}

