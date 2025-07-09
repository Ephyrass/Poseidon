package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;
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
 * Spring MVC controller for managing RuleName entities.
 * Provides endpoints to list, add, update, and delete rule names.
 */
@Controller
public class RuleNameController {
    private final RuleNameService ruleNameService;

    /**
     * Constructor for dependency injection of RuleNameService.
     * @param ruleNameService the service handling RuleName business logic
     */
    public RuleNameController(RuleNameService ruleNameService) {
        this.ruleNameService = ruleNameService;
    }

    /**
     * Displays the list of all RuleNames.
     * @param model Spring MVC model
     * @return the ruleName/list view
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        model.addAttribute("ruleNames", ruleNameService.findAll());
        return "ruleName/list";
    }

    /**
     * Shows the form to add a new RuleName.
     * @return the ruleName/add view
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm() {
        return "ruleName/add";
    }

    /**
     * Validates and saves a new RuleName entity.
     * @param ruleName the RuleName entity to validate and save
     * @param result BindingResult for validation errors
     * @return redirects to the rule name list if successful, otherwise returns the add form
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     * Shows the update form for a RuleName entity.
     * @param id the ID of the RuleName to update
     * @param model Spring MVC model
     * @return the update form view if found, otherwise redirects to the rule name list
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<RuleName> ruleName = ruleNameService.findById(id);
        if (ruleName.isPresent()) {
            model.addAttribute("ruleName", ruleName.get());
            return "ruleName/update";
        } else {
            model.addAttribute("errorMessage", "RuleName not found.");
            return "redirect:/ruleName/list";
        }
    }

    /**
     * Updates an existing RuleName entity.
     * @param id the ID of the RuleName to update
     * @param ruleName the updated RuleName entity
     * @param result BindingResult for validation errors
     * @return redirects to the rule name list if successful, otherwise returns the update form
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleName.setId(id);
        ruleNameService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     * Deletes a RuleName entity by its ID.
     * Checks if the entity exists before deleting and adds an error message if not found.
     * @param id the ID of the RuleName to delete
     * @param model Spring MVC model
     * @return redirects to the rule name list after deletion or if not found
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        if (ruleNameService.existsById(id)) {
            ruleNameService.deleteById(id);
        } else {
            model.addAttribute("errorMessage", "RuleName not found for deletion.");
        }
        return "redirect:/ruleName/list";
    }
}
