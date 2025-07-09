package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

/**
 * Spring MVC controller for managing CurvePoint entities.
 * Provides endpoints to list, add, update, and delete curve points.
 */
@Controller
public class CurveController {
    private final CurvePointService curvePointService;

    /**
     * Constructor for dependency injection of CurvePointService.
     * @param curvePointService the service handling CurvePoint business logic
     */
    public CurveController(CurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }

    /**
     * Displays the list of all CurvePoints.
     * @param model Spring MVC model
     * @return the curvePoint/list view
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "curvePoint/list";
    }

    /**
     * Shows the form to add a new CurvePoint.
     * @return the curvePoint/add view
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm() {
        return "curvePoint/add";
    }

    /**
     * Validates and saves a new CurvePoint entity.
     * @param curvePoint the CurvePoint entity to validate and save
     * @param result BindingResult for validation errors
     * @return redirects to the curve point list if successful, otherwise returns the add form
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Shows the update form for a CurvePoint entity.
     * @param id the ID of the CurvePoint to update
     * @param model Spring MVC model
     * @return the update form view if found, otherwise redirects to the curve point list
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<CurvePoint> curvePoint = curvePointService.findById(id);
        if (curvePoint.isPresent()) {
            model.addAttribute("curvePoint", curvePoint.get());
            return "curvePoint/update";
        } else {
            model.addAttribute("errorMessage", "CurvePoint not found.");
            return "redirect:/curvePoint/list";
        }
    }

    /**
     * Updates an existing CurvePoint entity.
     * @param id the ID of the CurvePoint to update
     * @param curvePoint the updated CurvePoint entity
     * @param result BindingResult for validation errors
     * @return redirects to the curve point list if successful, otherwise returns the update form
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePoint.setId(id);
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Deletes a CurvePoint entity by its ID.
     * Checks if the entity exists before deleting and adds an error message if not found.
     * @param id the ID of the CurvePoint to delete
     * @param model Spring MVC model
     * @return redirects to the curve point list after deletion or if not found
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        if (curvePointService.existsById(id)) {
            curvePointService.deleteById(id);
        } else {
            model.addAttribute("errorMessage", "CurvePoint not found for deletion.");
        }
        return "redirect:/curvePoint/list";
    }
}
