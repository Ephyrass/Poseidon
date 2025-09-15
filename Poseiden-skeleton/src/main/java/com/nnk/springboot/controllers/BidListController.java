package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

/**
 * Spring MVC controller for managing BidList entities.
 * Provides endpoints to list, add, update, and delete bid lists.
 * Endpoints are expected to be protected by Spring Security according to application configuration.
 */
@Controller
public class BidListController {
    private final BidListService bidListService;

    @Autowired
    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    /**
     * Display the list of all BidList entries.
     *
     * @param model Spring MVC model
     * @return the bidList/list view
     */
    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    /**
     * Show the form to create a new BidList.
     *
     * @return the bidList/add view
     */
    @GetMapping("/bidList/add")
    public String addBidForm() {
        return "bidList/add";
    }

    /**
     * Validate and save a new BidList entity.
     *
     * @param bid the BidList entity to validate and save
     * @param result binding result for validation errors
     * @return redirect to list on success, or add form on validation error
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.save(bid);
        return "redirect:/bidList/list";
    }

    /**
     * Show the update form for an existing BidList.
     *
     * @param id the id of the BidList to update
     * @param model Spring MVC model
     * @return update form view if found, otherwise redirect to list
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> bid = bidListService.findById(id);
        if (bid.isPresent()) {
            model.addAttribute("bidList", bid.get());
            return "bidList/update";
        } else {
            return "redirect:/bidList/list";
        }
    }

    /**
     * Update an existing BidList entity.
     *
     * @param id the id of the BidList to update
     * @param bidList the updated BidList entity
     * @param result binding result for validation errors
     * @return redirect to list on success, or update form on validation error
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidList.setId(id);
        bidListService.save(bidList);
        return "redirect:/bidList/list";
    }

    /**
     * Delete a BidList entity by id.
     *
     * @param id the id of the BidList to delete
     * @param model Spring MVC model
     * @return redirect to list after deletion or if not found
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        if (bidListService.existsById(id)) {
            bidListService.deleteById(id);
        } else {
            model.addAttribute("errorMessage", "Bid not found for deletion.");
        }
        return "redirect:/bidList/list";
    }
}
