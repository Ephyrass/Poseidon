package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
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
 * Spring MVC controller for managing Trade entities.
 * Provides endpoints to list, add, update, and delete trades.
 */
@Controller
public class TradeController {
    private final TradeService tradeService;

    /**
     * Constructor for dependency injection of TradeService.
     * @param tradeService the service handling Trade business logic
     */
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * Displays the list of all Trades.
     * @param model Spring MVC model
     * @return the trade/list view
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        model.addAttribute("trades", tradeService.findAll());
        return "trade/list";
    }

    /**
     * Shows the form to add a new Trade.
     * @return the trade/add view
     */
    @GetMapping("/trade/add")
    public String addTradeForm() {
        return "trade/add";
    }

    /**
     * Validates and saves a new Trade entity.
     * @param trade the Trade entity to validate and save
     * @param result BindingResult for validation errors
     * @return redirects to the trade list if successful, otherwise returns the add form
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    /**
     * Shows the update form for a Trade entity.
     * @param id the ID of the Trade to update
     * @param model Spring MVC model
     * @return the update form view if found, otherwise redirects to the trade list
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Trade> trade = tradeService.findById(id);
        if (trade.isPresent()) {
            model.addAttribute("trade", trade.get());
            return "trade/update";
        } else {
            model.addAttribute("errorMessage", "Trade not found.");
            return "redirect:/trade/list";
        }
    }

    /**
     * Updates an existing Trade entity.
     * @param id the ID of the Trade to update
     * @param trade the updated Trade entity
     * @param result BindingResult for validation errors
     * @return redirects to the trade list if successful, otherwise returns the update form
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        trade.setTradeId(id);
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    /**
     * Deletes a Trade entity by its ID.
     * Checks if the entity exists before deleting and adds an error message if not found.
     * @param id the ID of the Trade to delete
     * @param model Spring MVC model
     * @return redirects to the trade list after deletion or if not found
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        if (tradeService.existsById(id)) {
            tradeService.deleteById(id);
        } else {
            model.addAttribute("errorMessage", "Trade not found for deletion.");
        }
        return "redirect:/trade/list";
    }
}
