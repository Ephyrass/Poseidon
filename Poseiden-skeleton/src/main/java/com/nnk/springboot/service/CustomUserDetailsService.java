package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service personnalisé pour la gestion des détails d'authentification des utilisateurs.
 * Implémente UserDetailsService de Spring Security pour l'authentification session-based.
 *
 * @author Poseidon Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge les détails d'un utilisateur par son nom d'utilisateur pour l'authentification.
     * Cette méthode est utilisée par Spring Security lors du processus d'authentification
     * pour récupérer les informations de l'utilisateur et ses autorisations.
     *
     * @param username le nom d'utilisateur unique de l'utilisateur à authentifier
     * @return UserDetails contenant les informations d'authentification de l'utilisateur
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé avec ce nom d'utilisateur
     *
     * @see UserDetailsService#loadUserByUsername(String)
     * @see UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
