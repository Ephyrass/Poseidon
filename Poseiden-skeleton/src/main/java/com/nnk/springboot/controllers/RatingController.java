package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
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
 * Spring MVC controller for managing Rating entities.
 * Provides endpoints to list, add, update, and delete ratings.
 */
@Controller
public class RatingController {
    private final RatingService ratingService;

    /**
     * Constructor for dependency injection of RatingService.
     * @param ratingService the service handling Rating business logic
     */
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Displays the list of all Ratings.
     * @param model Spring MVC model
     * @return the rating/list view
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.findAll());
        return "rating/list";
    }

    /**
     * Shows the form to add a new Rating.
     * @return the rating/add view
     */
    @GetMapping("/rating/add")
    public String addRatingForm() {
        return "rating/add";
    }

    /**
     * Validates and saves a new Rating entity.
     * @param rating the Rating entity to validate and save
     * @param result BindingResult for validation errors
     * @return redirects to the rating list if successful, otherwise returns the add form
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    /**
     * Shows the update form for a Rating entity.
     * @param id the ID of the Rating to update
     * @param model Spring MVC model
     * @return the update form view if found, otherwise redirects to the rating list
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> rating = ratingService.findById(id);
        if (rating.isPresent()) {
            model.addAttribute("rating", rating.get());
            return "rating/update";
        } else {
            model.addAttribute("errorMessage", "Rating not found.");
            return "redirect:/rating/list";
        }
    }

    /**
     * Updates an existing Rating entity.
     * @param id the ID of the Rating to update
     * @param rating the updated Rating entity
     * @param result BindingResult for validation errors
     * @return redirects to the rating list if successful, otherwise returns the update form
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        rating.setId(id);
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    /**
     * Deletes a Rating entity by its ID.
     * Checks if the entity exists before deleting and adds an error message if not found.
     * @param id the ID of the Rating to delete
     * @param model Spring MVC model
     * @return redirects to the rating list after deletion or if not found
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        if (ratingService.existsById(id)) {
            ratingService.deleteById(id);
        } else {
            model.addAttribute("errorMessage", "Rating not found for deletion.");
        }
        return "redirect:/rating/list";
    }
}
