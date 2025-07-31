package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new user or updates an existing user.
     * The password is automatically encoded with BCrypt before saving
     * to ensure the security of authentication data.
     *
     * @param user the user to save (must not be null)
     * @return the saved user with its generated ID
     * @throws IllegalArgumentException if the user is null
     *
     * @see PasswordEncoder#encode(CharSequence)
     */
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }

        // Encoder le mot de passe avant la sauvegarde pour la sécurité
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Met à jour un utilisateur existant avec un nouveau mot de passe.
     * Le nouveau mot de passe est encodé automatiquement avant la mise à jour.
     *
     * @param user l'utilisateur à mettre à jour
     * @param newPassword le nouveau mot de passe en clair
     * @return l'utilisateur mis à jour
     * @throws IllegalArgumentException si l'utilisateur ou le mot de passe est null/vide
     */
    public User updateUserWithNewPassword(User user, String newPassword) {
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne peut pas être vide");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    /**
     * Recherche un utilisateur par son identifiant unique.
     *
     * @param id l'identifiant unique de l'utilisateur (ne doit pas être null)
     * @return un Optional contenant l'utilisateur s'il existe, Optional.empty() sinon
     * @throws IllegalArgumentException si l'identifiant est null
     */
    public Optional<User> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null");
        }
        return userRepository.findById(id);
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur (ne doit pas être null ou vide)
     * @return un Optional contenant l'utilisateur s'il existe, Optional.empty() sinon
     * @throws IllegalArgumentException si le nom d'utilisateur est null ou vide
     */
    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        return userRepository.findByUsername(username);
    }

    /**
     * Récupère tous les utilisateurs enregistrés dans le système.
     *
     * @return un Iterable contenant tous les utilisateurs
     */
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Supprime un utilisateur par son identifiant unique.
     * La suppression est définitive et ne peut pas être annulée.
     *
     * @param id l'identifiant unique de l'utilisateur à supprimer (ne doit pas être null)
     * @throws IllegalArgumentException si l'identifiant est null
     */
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null");
        }
        userRepository.deleteById(id);
    }

    /**
     * Vérifie si un utilisateur existe avec l'identifiant donné.
     *
     * @param id l'identifiant unique de l'utilisateur (ne doit pas être null)
     * @return true si l'utilisateur existe, false sinon
     * @throws IllegalArgumentException si l'identifiant est null
     */
    public boolean existsById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null");
        }
        return userRepository.existsById(id);
    }

    /**
     * Vérifie si un nom d'utilisateur est déjà utilisé dans le système.
     * Utile pour valider l'unicité lors de la création d'un nouvel utilisateur.
     *
     * @param username le nom d'utilisateur à vérifier (ne doit pas être null ou vide)
     * @return true si le nom d'utilisateur existe déjà, false sinon
     * @throws IllegalArgumentException si le nom d'utilisateur est null ou vide
     */
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        return userRepository.findByUsername(username).isPresent();
    }
}
