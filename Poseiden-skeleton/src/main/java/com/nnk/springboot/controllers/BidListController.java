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


@Controller
public class BidListController {
    private final BidListService bidListService;

    @Autowired
    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm() {
        return "bidList/add";
    }

    /**
     * Validate and save a new BidList entity.
     * @param bid The BidList entity to validate and save
     * @param result BindingResult for validation errors
     * @return Redirects to the bid list page if successful, otherwise returns the add form
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
     * Show the update form for a BidList entity.
     * @param id The ID of the BidList to update
     * @param model Spring MVC model
     * @return The update form view if found, otherwise redirects to the bid list
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
     * @param id The ID of the BidList to update
     * @param bidList The updated BidList entity
     * @param result BindingResult for validation errors
     * @return Redirects to the bid list page if successful, otherwise returns the update form
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
     * Delete a BidList entity by its ID.
     * Checks if the entity exists before deleting and adds an error message if not found.
     * @param id The ID of the BidList to delete
     * @param model Spring MVC model
     * @return Redirects to the bid list page after deletion or if not found
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
