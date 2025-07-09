package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * Spring MVC controller for managing User entities.
 * Provides endpoints to list, add, update, and delete users.
 */
@Controller
public class UserController {
    private final UserService userService;

    /**
     * Constructor for dependency injection of UserService.
     * @param userService the service handling User business logic
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the list of all Users.
     * @param model Spring MVC model
     * @return the user/list view
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Shows the form to add a new User.
     * @param model Spring MVC model
     * @return the user/add view
     */
    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/add";
    }

    /**
     * Validates and saves a new User entity (with password encoding).
     * @param user the User entity to validate and save
     * @param result BindingResult for validation errors
     * @return redirects to the user list if successful, otherwise returns the add form
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/add";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            userService.save(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Username already exists. Please choose another username.");
            return "user/add";
        }
        return "redirect:/user/list";
    }

    /**
     * Shows the update form for a User entity.
     * @param id the ID of the User to update
     * @param model Spring MVC model
     * @return the update form view if found, otherwise redirects to the user list
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setPassword("");
            model.addAttribute("user", u);
            return "user/update";
        } else {
            model.addAttribute("errorMessage", "User not found.");
            return "redirect:/user/list";
        }
    }

    /**
     * Updates an existing User entity (with password encoding).
     * @param id the ID of the User to update
     * @param user the updated User entity
     * @param result BindingResult for validation errors
     * @return redirects to the user list if successful, otherwise returns the update form
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "user/update";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.save(user);
        return "redirect:/user/list";
    }

    /**
     * Deletes a User entity by its ID.
     * Checks if the entity exists before deleting and adds an error message if not found.
     * @param id the ID of the User to delete
     * @param model Spring MVC model
     * @return redirects to the user list after deletion or if not found
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
