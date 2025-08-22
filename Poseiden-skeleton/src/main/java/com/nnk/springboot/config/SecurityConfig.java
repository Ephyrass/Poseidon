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
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

/**
 * Security configuration for the Poseidon application.
 * Configures session-based authentication, access permissions,
 * session management, and custom pages.
 *
 * <p>This configuration implements security best practices including:
 * <ul>
 *   <li>Session-based authentication</li>
 *   <li>Session management with limitation to one session per user</li>
 *   <li>CSRF protection (disabled for H2 console)</li>
 *   <li>Password encoding with BCrypt</li>
 *   <li>Custom error pages</li>
 * </ul>
 *
 * @author Poseidon Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain to handle HTTP permissions,
     * form-based authentication, session management, and error pages.
     *
     * <p>Access configuration:
     * <ul>
     *   <li>Public static resources: /, /login, /css/**, /js/**, /images/**, /user/add, /user/validate</li>
     *   <li>Restricted H2 console access to ADMIN role only</li>
     *   <li>All other resources require authentication</li>
     * </ul>
     *
     * <p>Session management:
     * <ul>
     *   <li>Maximum of one active session per user</li>
     *   <li>Redirect to /login?expired if the session expires</li>
     *   <li>Automatic invalidation of previous sessions</li>
     * </ul>
     *
     * @param http the HttpSecurity object to configure web security
     * @return the configured security filter
     * @throws Exception in case of configuration error
     *
     * @see HttpSecurity
     * @see SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Public static resources
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // Specific public pages
                .requestMatchers("/", "/login").permitAll()
                // User management - ADMIN only
                .requestMatchers("/user/**").hasRole("ADMIN")
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints if needed
            .headers(headers -> headers
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true)
                ) // HSTS to enforce HTTPS in production
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/home", true) // Redirect after successful login to home page
                .failureUrl("/login?error") // Redirect on login failure
                .permitAll() // Free access to login page
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Explicit URL for logout
                .logoutSuccessUrl("/login?logout") // Redirect after logout
                .invalidateHttpSession(true) // Invalidate HTTP session
                .deleteCookies("JSESSIONID") // Remove session cookie
                .permitAll() // Free access to logout
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403") // Access denied error page
            )
            .sessionManagement(session -> session
                .maximumSessions(1) // Limit to one session per user
                .maxSessionsPreventsLogin(false) // Allow new login by invalidating old session
                .expiredUrl("/login?expired") // Redirect if session expires
                .sessionRegistry(sessionRegistry()) // Add session registry
            );
        return http.build();
    }

    /**
     * Provides a password encoder based on the BCrypt algorithm.
     * BCrypt is an adaptive hashing algorithm based on Blowfish,
     * specifically designed for secure password hashing.
     *
     * <p>Advantages of BCrypt:
     * <ul>
     *   <li>Resistant to brute-force attacks</li>
     *   <li>Configurable adaptive cost</li>
     *   <li>Automatic salt generation</li>
     *   <li>Industry standard for password hashing</li>
     * </ul>
     *
     * @return an instance of BCryptPasswordEncoder with default strength (10)
     *
     * @see BCryptPasswordEncoder
     * @see PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the authentication manager for the application.
     * The authentication manager is responsible for validating
     * user credentials.
     *
     * @param authenticationConfiguration Spring authentication configuration
     * @return the configured authentication manager
     * @throws Exception in case of configuration error
     *
     * @see AuthenticationManager
     * @see AuthenticationConfiguration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean for publishing HTTP session events.
     * Necessary for the proper functioning of concurrent session management.
     * Allows Spring Security to detect session creation and destruction.
     *
     * @return the HTTP session event publisher
     *
     * @see HttpSessionEventPublisher
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * Provides the registry of sessions for managing active sessions.
     * The session registry keeps track of active sessions and
     * manages restrictions such as the maximum number of sessions per user.
     *
     * @return an instance of SessionRegistryImpl for session tracking
     *
     * @see SessionRegistry
     * @see SessionRegistryImpl
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
