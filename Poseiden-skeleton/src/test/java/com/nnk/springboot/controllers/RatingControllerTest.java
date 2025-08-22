package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for RatingController.
 * Tests all endpoints for credit rating management.
 */
@WebMvcTest(controllers = RatingController.class)
@DisplayName("RatingController Integration Tests")
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    private Rating testRating;

    @BeforeEach
    void setUp() {
        testRating = Rating.builder()
                .id(1)
                .moodysRating("Aaa")
                .sandPRating("AAA")
                .fitchRating("AAA")
                .orderNumber(1)
                .build();
    }

    @Test
    @DisplayName("Should display rating list successfully")
    @WithMockUser(roles = "USER")
    void testRatingListPage() throws Exception {
        // Given
        when(ratingService.findAll()).thenReturn(Collections.singletonList(testRating));

        // When & Then
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"));

        verify(ratingService).findAll();
    }

    @Test
    @DisplayName("Should show add rating form")
    @WithMockUser(roles = "USER")
    void testShowAddRatingForm() throws Exception {
        // When & Then
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @DisplayName("Should validate and save new rating successfully")
    @WithMockUser(roles = "USER")
    void testValidateRatingSuccess() throws Exception {
        // Given
        when(ratingService.save(any(Rating.class))).thenReturn(testRating);

        // When & Then
        mockMvc.perform(post("/rating/validate")
                .with(csrf())
                .param("moodysRating", "Aa1")
                .param("sandPRating", "AA+")
                .param("fitchRating", "AA+")
                .param("orderNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).save(any(Rating.class));
    }

    @Test
    @DisplayName("Should show validation errors for invalid rating data")
    @WithMockUser(roles = "USER")
    void testValidateRatingWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/rating/validate")
                .with(csrf())
                .param("moodysRating", "")  // Invalid empty rating
                .param("sandPRating", "")   // Invalid empty rating
                .param("fitchRating", "")   // Invalid empty rating
                .param("orderNumber", "-1")) // Invalid negative order
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));

        verify(ratingService, never()).save(any(Rating.class));
    }

    @Test
    @DisplayName("Should show update form for existing rating")
    @WithMockUser(roles = "USER")
    void testShowUpdateForm() throws Exception {
        // Given
        when(ratingService.findById(1)).thenReturn(Optional.of(testRating));

        // When & Then
        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"));

        verify(ratingService).findById(1);
    }

    @Test
    @DisplayName("Should update rating successfully")
    @WithMockUser(roles = "USER")
    void testUpdateRatingSuccess() throws Exception {
        // Given
        when(ratingService.findById(1)).thenReturn(Optional.of(testRating));
        when(ratingService.save(any(Rating.class))).thenReturn(testRating);

        // When & Then
        mockMvc.perform(post("/rating/update/1")
                .with(csrf())
                .param("moodysRating", "Baa2")
                .param("sandPRating", "BBB")
                .param("fitchRating", "BBB-")
                .param("orderNumber", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).save(any(Rating.class));
    }

    @Test
    @DisplayName("Should delete rating successfully")
    @WithMockUser(roles = "USER")
    void testDeleteRating() throws Exception {
        // Given
        when(ratingService.existsById(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).deleteById(1);
    }

    @Test
    @DisplayName("Should validate different rating agency formats")
    @WithMockUser(roles = "USER")
    void testValidateRatingAgencyFormats() throws Exception {
        // Given
        when(ratingService.save(any(Rating.class))).thenReturn(testRating);

        // Test Moody's format
        mockMvc.perform(post("/rating/validate")
                .with(csrf())
                .param("moodysRating", "Caa1")  // Moody's format
                .param("sandPRating", "CCC+")   // S&P format
                .param("fitchRating", "CCC")    // Fitch format
                .param("orderNumber", "10"))
                .andExpect(status().is3xxRedirection());

        verify(ratingService).save(any(Rating.class));
    }
}
