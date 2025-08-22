package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
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
 * Integration tests for TradeController.
 * Tests all endpoints for trade management.
 */
@WebMvcTest(controllers = TradeController.class)
@DisplayName("TradeController Integration Tests")
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    private Trade testTrade;

    @BeforeEach
    void setUp() {
        testTrade = Trade.builder()
                .tradeId(1)
                .account("Test Account")
                .type("Test Type")
                .buyQuantity(100.0)
                .sellQuantity(90.0)
                .buyPrice(50.0)
                .sellPrice(55.0)
                .benchmark("Test Benchmark")
                .book("Test Book")
                .creationName("Test Creator")
                .security("Test Security")
                .status("Active")
                .trader("Test Trader")
                .build();
    }

    @Test
    @DisplayName("Should display trade list successfully")
    @WithMockUser(roles = "USER")
    void testTradeListPage() throws Exception {
        // Given
        when(tradeService.findAll()).thenReturn(Collections.singletonList(testTrade));

        // When & Then
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));

        verify(tradeService).findAll();
    }

    @Test
    @DisplayName("Should show add trade form")
    @WithMockUser(roles = "USER")
    void testShowAddTradeForm() throws Exception {
        // When & Then
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @DisplayName("Should validate and save new trade successfully")
    @WithMockUser(roles = "USER")
    void testValidateTradeSuccess() throws Exception {
        // Given
        when(tradeService.save(any(Trade.class))).thenReturn(testTrade);

        // When & Then
        mockMvc.perform(post("/trade/validate")
                .with(csrf())
                .param("account", "New Account")
                .param("type", "New Type")
                .param("buyQuantity", "150.0")
                .param("sellQuantity", "140.0")
                .param("buyPrice", "60.0")
                .param("sellPrice", "65.0")
                .param("benchmark", "New Benchmark")
                .param("book", "New Book")
                .param("creationName", "New Creator")
                .param("security", "New Security")
                .param("status", "New")
                .param("trader", "New Trader"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).save(any(Trade.class));
    }

    @Test
    @DisplayName("Should show validation errors for invalid trade data")
    @WithMockUser(roles = "USER")
    void testValidateTradeWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/trade/validate")
                .with(csrf())
                .param("account", "")       // Invalid empty account
                .param("type", "")          // Invalid empty type
                .param("buyQuantity", "-10")) // Invalid negative quantity
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));

        verify(tradeService, never()).save(any(Trade.class));
    }

    @Test
    @DisplayName("Should show update form for existing trade")
    @WithMockUser(roles = "USER")
    void testShowUpdateForm() throws Exception {
        // Given
        when(tradeService.findById(1)).thenReturn(Optional.of(testTrade));

        // When & Then
        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));

        verify(tradeService).findById(1);
    }

    @Test
    @DisplayName("Should redirect when trade not found for update")
    @WithMockUser(roles = "USER")
    void testShowUpdateFormTradeNotFound() throws Exception {
        // Given
        when(tradeService.findById(99)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/trade/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).findById(99);
    }

    @Test
    @DisplayName("Should update trade successfully")
    @WithMockUser(roles = "USER")
    void testUpdateTradeSuccess() throws Exception {
        // Given
        when(tradeService.save(any(Trade.class))).thenReturn(testTrade);

        // When & Then
        mockMvc.perform(post("/trade/update/1")
                .with(csrf())
                .param("account", "Updated Account")
                .param("type", "Updated Type")
                .param("buyQuantity", "200.0")
                .param("sellQuantity", "190.0")
                .param("buyPrice", "70.0")
                .param("sellPrice", "75.0")
                .param("benchmark", "Updated Benchmark")
                .param("book", "Updated Book")
                .param("creationName", "Updated Creator")
                .param("security", "Updated Security")
                .param("status", "Updated")
                .param("trader", "Updated Trader"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).save(any(Trade.class));
    }

    @Test
    @DisplayName("Should show validation errors when updating with invalid data")
    @WithMockUser(roles = "USER")
    void testUpdateTradeWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/trade/update/1")
                .with(csrf())
                .param("account", "")       // Invalid empty account
                .param("type", "")          // Invalid empty type
                .param("buyQuantity", "-10")) // Invalid negative quantity
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));

        verify(tradeService, never()).save(any(Trade.class));
    }

    @Test
    @DisplayName("Should delete trade successfully")
    @WithMockUser(roles = "USER")
    void testDeleteTrade() throws Exception {
        // Given
        when(tradeService.existsById(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).deleteById(1);
    }

    @Test
    @DisplayName("Should handle delete request for non-existing trade")
    @WithMockUser(roles = "USER")
    void testDeleteNonExistingTrade() throws Exception {
        // Given
        when(tradeService.existsById(99)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/trade/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, never()).deleteById(anyInt());
    }
}
