package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        UserRepository repo = mock(UserRepository.class);
        User user = User.builder()
                .username("john")
                .password("secret")
                .role("USER")
                .fullname("John Doe")
                .build();
        when(repo.findByUsername("john")).thenReturn(Optional.of(user));

        CustomUserDetailsService service = new CustomUserDetailsService(repo);
        UserDetails details = service.loadUserByUsername("john");

        assertEquals("john", details.getUsername());
        assertEquals("secret", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_shouldThrow_whenUserMissing() {
        UserRepository repo = mock(UserRepository.class);
        when(repo.findByUsername("missing")).thenReturn(Optional.empty());

        CustomUserDetailsService service = new CustomUserDetailsService(repo);
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("missing"));
    }
}

