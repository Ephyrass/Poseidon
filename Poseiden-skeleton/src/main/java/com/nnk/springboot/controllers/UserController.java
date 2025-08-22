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
 * Spring MVC controller for user management with BCrypt authentication.
 * Uses the new UserService methods for consistent password encoding.
 */
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the list of all users.
     *
     * @param model Spring MVC model to pass attributes to the view
     * @return the user list view
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Shows the form to add a new user.
     *
     * @param model Spring MVC model to pass attributes to the view
     * @return the add user view
     */
    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/add";
    }

    /**
     * Validates and saves a new user.
     * Uses saveWithPasswordEncoding to automatically encode the password.
     *
     * @param user User entity to validate and save
     * @param result BindingResult for validation errors
     * @param model Spring MVC model to pass attributes to the view
     * @return redirect to user list on success, or add user view on error
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

    /**
     * Displays the form to update an existing user.
     *
     * @param id    ID of the user to update
     * @param model Spring MVC model to pass attributes to the view
     * @return the update user view, or redirect to user list on error
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setPassword(""); // Clear password for security
            model.addAttribute("user", u);
            return "user/update";
        } else {
            model.addAttribute("errorMessage", "User not found.");
            return "redirect:/user/list";
        }
    }

    /**
     * Updates an existing user.
     * Uses updateUserWithNewPassword to encode the new password.
     *
     * @param id      ID of the user to update
     * @param user    User entity with updated information
     * @param result  BindingResult for validation errors
     * @param model   Spring MVC model to pass attributes to the view
     * @return redirect to user list on success, or update user view on error
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        // Check if the username already exists for another user
        Optional<User> existingUserWithSameUsername = userService.findByUsername(user.getUsername());
        if (existingUserWithSameUsername.isPresent() && !existingUserWithSameUsername.get().getId().equals(id)) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Username already exists. Please choose another username.");
            return "user/update";
        }

        try {
            // Retrieve the existing user and update with the new password
            Optional<User> existingUser = userService.findById(id);
            if (existingUser.isPresent()) {
                User userToUpdate = existingUser.get();
                userToUpdate.setUsername(user.getUsername());
                userToUpdate.setFullname(user.getFullname());
                userToUpdate.setRole(user.getRole());

                // Use the specialized method to update with a new password
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

    /**
     * Deletes a user by ID.
     *
     * @param id    ID of the user to delete
     * @param model Spring MVC model to pass attributes to the view
     * @return redirect to user list
     */
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
