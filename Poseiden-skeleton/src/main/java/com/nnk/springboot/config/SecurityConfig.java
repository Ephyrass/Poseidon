package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Configuration de sécurité pour l'application Poseidon.
 * Configure l'authentification session-based, les autorisations d'accès,
 * la gestion des sessions et les pages personnalisées.
 *
 * <p>Cette configuration implémente les bonnes pratiques de sécurité incluant :
 * <ul>
 *   <li>Authentification basée sur les sessions</li>
 *   <li>Gestion des sessions avec limitation à une session par utilisateur</li>
 *   <li>Protection CSRF (désactivée pour H2 console)</li>
 *   <li>Encodage des mots de passe avec BCrypt</li>
 *   <li>Pages d'erreur personnalisées</li>
 * </ul>
 *
 * @author Poseidon Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class SecurityConfig {

    /**
     * Configure la chaîne de filtres de sécurité pour gérer les autorisations HTTP,
     * l'authentification par formulaire, la gestion des sessions et les pages d'erreur.
     *
     * <p>Configuration des accès :
     * <ul>
     *   <li>Ressources publiques : /, /login, /css/**, /js/**, /images/**, /user/add, /user/validate</li>
     *   <li>Console H2 : accès restreint au rôle ADMIN uniquement</li>
     *   <li>Toutes les autres ressources : authentification requise</li>
     * </ul>
     *
     * <p>Gestion des sessions :
     * <ul>
     *   <li>Maximum d'une session active par utilisateur</li>
     *   <li>Redirection vers /login?expired si la session expire</li>
     *   <li>Invalidation automatique des sessions précédentes</li>
     * </ul>
     *
     * @param http l'objet HttpSecurity pour configurer la sécurité web
     * @return le filtre de sécurité configuré
     * @throws Exception en cas d'erreur de configuration
     *
     * @see HttpSecurity
     * @see SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/user/add", "/user/validate").permitAll() // Ressources publiques
                .requestMatchers("/h2-console/**").hasRole("ADMIN") // Console H2 restreinte aux ADMIN
                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // Désactiver CSRF pour H2 console uniquement
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Autoriser les frames du même origine
            )
            .formLogin(form -> form
                .loginPage("/login") // Page de connexion personnalisée
                .defaultSuccessUrl("/home", true) // Redirection après connexion réussie vers la page d'accueil
                .failureUrl("/login?error") // Redirection en cas d'échec de connexion
                .permitAll() // Accès libre à la page de connexion
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL explicite pour le logout
                .logoutSuccessUrl("/login?logout") // Redirection après déconnexion
                .invalidateHttpSession(true) // Invalider la session HTTP
                .deleteCookies("JSESSIONID") // Supprimer le cookie de session
                .permitAll() // Accès libre à la déconnexion
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403") // Page d'erreur d'accès refusé
            )
            .sessionManagement(session -> session
                .maximumSessions(1) // Limite à une session par utilisateur
                .maxSessionsPreventsLogin(false) // Permettre la nouvelle connexion en invalidant l'ancienne session
                .expiredUrl("/login?expired") // Redirection si la session expire
            );
        return http.build();
    }

    /**
     * Fournit un encodeur de mot de passe basé sur l'algorithme BCrypt.
     * BCrypt est un algorithme de hachage adaptatif basé sur Blowfish,
     * conçu spécifiquement pour le hachage sécurisé des mots de passe.
     *
     * <p>Avantages de BCrypt :
     * <ul>
     *   <li>Résistant aux attaques par force brute</li>
     *   <li>Coût adaptatif configurable</li>
     *   <li>Génération automatique de sel (salt)</li>
     *   <li>Standard de l'industrie pour le hachage des mots de passe</li>
     * </ul>
     *
     * @return une instance de BCryptPasswordEncoder avec la force par défaut (10)
     *
     * @see BCryptPasswordEncoder
     * @see PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit le gestionnaire d'authentification pour l'application.
     * Le gestionnaire d'authentification est responsable de la validation
     * des informations d'identification des utilisateurs.
     *
     * @param authenticationConfiguration la configuration d'authentification Spring
     * @return le gestionnaire d'authentification configuré
     * @throws Exception en cas d'erreur de configuration
     *
     * @see AuthenticationManager
     * @see AuthenticationConfiguration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean pour publier les événements de session HTTP.
     * Nécessaire pour le bon fonctionnement de la gestion des sessions concurrentes.
     * Permet à Spring Security de détecter les créations et destructions de sessions.
     *
     * @return l'éditeur d'événements de session HTTP
     *
     * @see HttpSessionEventPublisher
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
