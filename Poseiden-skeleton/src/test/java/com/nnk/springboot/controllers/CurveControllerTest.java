package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
 * Integration tests for CurveController.
 * Tests all endpoints for curve point management.
 */
@WebMvcTest(controllers = CurveController.class)
@DisplayName("CurveController Integration Tests")
class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    private CurvePoint testCurvePoint;

    @BeforeEach
    void setUp() {
        testCurvePoint = CurvePoint.builder()
                .id(1)
                .curveId(1)
                .term(10.0)
                .value(30.0)
                .build();
    }

    @Test
    @DisplayName("Should display curve point list successfully")
    @WithMockUser(roles = "USER")
    void testCurvePointListPage() throws Exception {
        // Given
        when(curvePointService.findAll()).thenReturn(Collections.singletonList(testCurvePoint));

        // When & Then
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"));

        verify(curvePointService).findAll();
    }

    @Test
    @DisplayName("Should show add curve point form")
    @WithMockUser(roles = "USER")
    void testShowAddCurvePointForm() throws Exception {
        // When & Then
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @DisplayName("Should validate and save new curve point successfully")
    @WithMockUser(roles = "USER")
    void testValidateCurvePointSuccess() throws Exception {
        // Given
        when(curvePointService.save(any(CurvePoint.class))).thenReturn(testCurvePoint);

        // When & Then
        mockMvc.perform(post("/curvePoint/validate")
                .with(csrf())
                .param("curveId", "1")
                .param("term", "15.0")
                .param("value", "35.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).save(any(CurvePoint.class));
    }

    @Test
    @DisplayName("Should show validation errors for invalid curve point data")
    @WithMockUser(roles = "USER")
    void testValidateCurvePointWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/curvePoint/validate")
                .with(csrf())
                .param("curveId", "")     // Invalid empty curveId
                .param("term", "-5.0")    // Invalid negative term
                .param("value", ""))      // Invalid empty value
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));

        verify(curvePointService, never()).save(any(CurvePoint.class));
    }

    @Test
    @DisplayName("Should show update form for existing curve point")
    @WithMockUser(roles = "USER")
    void testShowUpdateForm() throws Exception {
        // Given
        when(curvePointService.findById(1)).thenReturn(Optional.of(testCurvePoint));

        // When & Then
        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));

        verify(curvePointService).findById(1);
    }

    @Test
    @DisplayName("Should redirect when curve point not found for update")
    @WithMockUser(roles = "USER")
    void testShowUpdateFormCurvePointNotFound() throws Exception {
        // Given
        when(curvePointService.findById(99)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/curvePoint/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).findById(99);
    }

    @Test
    @DisplayName("Should update curve point successfully")
    @WithMockUser(roles = "USER")
    void testUpdateCurvePointSuccess() throws Exception {
        // Given
        when(curvePointService.findById(1)).thenReturn(Optional.of(testCurvePoint));
        when(curvePointService.save(any(CurvePoint.class))).thenReturn(testCurvePoint);

        // When & Then
        mockMvc.perform(post("/curvePoint/update/1")
                .with(csrf())
                .param("curveId", "2")
                .param("term", "20.0")
                .param("value", "40.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).save(any(CurvePoint.class));
    }

    @Test
    @DisplayName("Should delete curve point successfully")
    @WithMockUser(roles = "USER")
    void testDeleteCurvePoint() throws Exception {
        // Given
        when(curvePointService.existsById(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).deleteById(1);
    }
}
