package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import java.util.Optional;

/**
 * Contrôleur Spring MVC pour la gestion des utilisateurs avec authentification BCrypt.
 * Utilise les nouvelles méthodes du UserService pour un encodage cohérent des mots de passe.
 */
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/add";
    }

    /**
     * Valide et sauvegarde un nouvel utilisateur.
     * Utilise saveWithPasswordEncoding pour encoder automatiquement le mot de passe.
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/add";
        }

        try {
            userService.saveWithPasswordEncoding(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Username already exists. Please choose another username.");
            return "user/add";
        }
        return "redirect:/user/list";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setPassword(""); // Vider le mot de passe pour la sécurité
            model.addAttribute("user", u);
            return "user/update";
        } else {
            model.addAttribute("errorMessage", "User not found.");
            return "redirect:/user/list";
        }
    }

    /**
     * Met à jour un utilisateur existant.
     * Utilise updateUserWithNewPassword pour encoder le nouveau mot de passe.
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        // Vérifier si le username existe déjà pour un autre utilisateur
        Optional<User> existingUserWithSameUsername = userService.findByUsername(user.getUsername());
        if (existingUserWithSameUsername.isPresent() && !existingUserWithSameUsername.get().getId().equals(id)) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Username already exists. Please choose another username.");
            return "user/update";
        }

        try {
            // Récupérer l'utilisateur existant et mettre à jour avec le nouveau mot de passe
            Optional<User> existingUser = userService.findById(id);
            if (existingUser.isPresent()) {
                User userToUpdate = existingUser.get();
                userToUpdate.setUsername(user.getUsername());
                userToUpdate.setFullname(user.getFullname());
                userToUpdate.setRole(user.getRole());

                // Utiliser la méthode spécialisée pour mettre à jour avec un nouveau mot de passe
                userService.updateUserWithNewPassword(userToUpdate, user.getPassword());
            } else {
                model.addAttribute("errorMessage", "User not found.");
                return "user/update";
            }
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Username already exists. Please choose another username.");
            return "user/update";
        }
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
        } else {
            model.addAttribute("errorMessage", "User not found for deletion.");
        }
        return "redirect:/user/list";
    }
}
