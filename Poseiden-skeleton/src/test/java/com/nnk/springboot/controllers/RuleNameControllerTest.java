package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;
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
 * Integration tests for RuleNameController.
 * Tests all endpoints for rule name management.
 */
@WebMvcTest(controllers = RuleNameController.class)
@DisplayName("RuleNameController Integration Tests")
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    private RuleName testRuleName;

    @BeforeEach
    void setUp() {
        testRuleName = RuleName.builder()
                .id(1)
                .name("Test Rule")
                .description("Test Description")
                .json("Test JSON")
                .template("Test Template")
                .sqlStr("SELECT * FROM test")
                .sqlPart("WHERE id = 1")
                .build();
    }

    @Test
    @DisplayName("Should display rule name list successfully")
    @WithMockUser(roles = "USER")
    void testRuleNameListPage() throws Exception {
        // Given
        when(ruleNameService.findAll()).thenReturn(Collections.singletonList(testRuleName));

        // When & Then
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"));

        verify(ruleNameService).findAll();
    }

    @Test
    @DisplayName("Should show add rule name form")
    @WithMockUser(roles = "USER")
    void testShowAddRuleNameForm() throws Exception {
        // When & Then
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @DisplayName("Should validate and save new rule name successfully")
    @WithMockUser(roles = "USER")
    void testValidateRuleNameSuccess() throws Exception {
        // Given
        when(ruleNameService.save(any(RuleName.class))).thenReturn(testRuleName);

        // When & Then
        mockMvc.perform(post("/ruleName/validate")
                .with(csrf())
                .param("name", "New Rule")
                .param("description", "New Description")
                .param("json", "New JSON")
                .param("template", "New Template")
                .param("sqlStr", "SELECT * FROM new")
                .param("sqlPart", "WHERE id = 2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).save(any(RuleName.class));
    }

    @Test
    @DisplayName("Should show validation errors for invalid rule name data")
    @WithMockUser(roles = "USER")
    void testValidateRuleNameWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/ruleName/validate")
                .with(csrf())
                .param("name", "")          // Invalid empty name
                .param("description", "")   // Invalid empty description
                .param("json", "")          // Invalid empty json
                .param("template", "")      // Invalid empty template
                .param("sqlStr", "")        // Invalid empty sqlStr
                .param("sqlPart", ""))      // Invalid empty sqlPart
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));

        verify(ruleNameService, never()).save(any(RuleName.class));
    }

    @Test
    @DisplayName("Should show update form for existing rule name")
    @WithMockUser(roles = "USER")
    void testShowUpdateForm() throws Exception {
        // Given
        when(ruleNameService.findById(1)).thenReturn(Optional.of(testRuleName));

        // When & Then
        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"));

        verify(ruleNameService).findById(1);
    }

    @Test
    @DisplayName("Should redirect when rule name not found for update")
    @WithMockUser(roles = "USER")
    void testShowUpdateFormRuleNameNotFound() throws Exception {
        // Given
        when(ruleNameService.findById(99)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/ruleName/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).findById(99);
    }

    @Test
    @DisplayName("Should update rule name successfully")
    @WithMockUser(roles = "USER")
    void testUpdateRuleNameSuccess() throws Exception {
        // Given
        when(ruleNameService.save(any(RuleName.class))).thenReturn(testRuleName);

        // When & Then
        mockMvc.perform(post("/ruleName/update/1")
                .with(csrf())
                .param("name", "Updated Rule")
                .param("description", "Updated Description")
                .param("json", "Updated JSON")
                .param("template", "Updated Template")
                .param("sqlStr", "SELECT * FROM updated")
                .param("sqlPart", "WHERE id = 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).save(any(RuleName.class));
    }

    @Test
    @DisplayName("Should show validation errors when updating with invalid data")
    @WithMockUser(roles = "USER")
    void testUpdateRuleNameWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/ruleName/update/1")
                .with(csrf())
                .param("name", "")          // Invalid empty name
                .param("description", "")   // Invalid empty description
                .param("json", "")          // Invalid empty json
                .param("template", "")      // Invalid empty template
                .param("sqlStr", "")        // Invalid empty sqlStr
                .param("sqlPart", ""))      // Invalid empty sqlPart
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));

        verify(ruleNameService, never()).save(any(RuleName.class));
    }

    @Test
    @DisplayName("Should delete rule name successfully")
    @WithMockUser(roles = "USER")
    void testDeleteRuleName() throws Exception {
        // Given
        when(ruleNameService.existsById(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).deleteById(1);
    }

    @Test
    @DisplayName("Should handle delete request for non-existing rule name")
    @WithMockUser(roles = "USER")
    void testDeleteNonExistingRuleName() throws Exception {
        // Given
        when(ruleNameService.existsById(99)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/ruleName/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, never()).deleteById(anyInt());
    }
}
