package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for BidListController.
 * Tests all endpoints with proper validation and business logic.
 */
@WebMvcTest(controllers = BidListController.class)
@DisplayName("BidListController Integration Tests")
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    private BidList testBidList;

    @BeforeEach
    void setUp() {
        testBidList = BidList.builder()
                .id(1)
                .account("Test Account")
                .type("Test Type")
                .bidQuantity(100.0)
                .build();
    }

    @Test
    @DisplayName("Should display bid list successfully")
    @WithMockUser(roles = "USER")
    void testBidListPage() throws Exception {
        // Given
        when(bidListService.findAll()).thenReturn(Collections.singletonList(testBidList));

        // When & Then
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"));

        verify(bidListService).findAll();
    }

    @Test
    @DisplayName("Should show add bid form")
    @WithMockUser(roles = "USER")
    void testShowAddBidForm() throws Exception {
        // When & Then
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @DisplayName("Should validate and save new bid successfully")
    @WithMockUser(roles = "USER")
    void testValidateBidSuccess() throws Exception {
        // Given
        when(bidListService.save(any(BidList.class))).thenReturn(testBidList);

        // When & Then
        mockMvc.perform(post("/bidList/validate")
                .with(csrf())
                .param("account", "New Account")
                .param("type", "New Type")
                .param("bidQuantity", "150.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).save(any(BidList.class));
    }

    @Test
    @DisplayName("Should show validation errors for invalid bid data")
    @WithMockUser(roles = "USER")
    void testValidateBidWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/bidList/validate")
                .with(csrf())
                .param("account", "") // Invalid empty account
                .param("type", "")    // Invalid empty type
                .param("bidQuantity", "-10")) // Invalid negative quantity
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));

        verify(bidListService, never()).save(any(BidList.class));
    }

    @Test
    @DisplayName("Should show update form for existing bid")
    @WithMockUser(roles = "USER")
    void testShowUpdateForm() throws Exception {
        // Given
        when(bidListService.findById(1)).thenReturn(Optional.of(testBidList));

        // When & Then
        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));

        verify(bidListService).findById(1);
    }

    @Test
    @DisplayName("Should redirect when bid not found for update")
    @WithMockUser(roles = "USER")
    void testShowUpdateFormBidNotFound() throws Exception {
        // Given
        when(bidListService.findById(99)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/bidList/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).findById(99);
    }

    @Test
    @DisplayName("Should update bid successfully")
    @WithMockUser(roles = "USER")
    void testUpdateBidSuccess() throws Exception {
        // Given
        when(bidListService.findById(1)).thenReturn(Optional.of(testBidList));
        when(bidListService.save(any(BidList.class))).thenReturn(testBidList);

        // When & Then
        mockMvc.perform(post("/bidList/update/1")
                .with(csrf())
                .param("account", "Updated Account")
                .param("type", "Updated Type")
                .param("bidQuantity", "200.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).save(any(BidList.class));
    }

    @Test
    @DisplayName("Should delete bid successfully")
    @WithMockUser(roles = "USER")
    void testDeleteBid() throws Exception {
        // Given
        when(bidListService.existsById(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).deleteById(1);
    }

    @Test
    @DisplayName("Should handle delete request for non-existing bid")
    @WithMockUser(roles = "USER")
    void testDeleteNonExistingBid() throws Exception {
        // Given
        when(bidListService.existsById(99)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/bidList/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, never()).deleteById(anyInt());
    }
}
