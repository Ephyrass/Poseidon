package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RuleNameService.
 * Tests all CRUD operations and business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RuleNameService Tests")
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    private RuleName testRuleName;

    @BeforeEach
    void setUp() {
        testRuleName = RuleName.builder()
                .id(1)
                .name("Test Rule")
                .description("Test Description")
                .json("{'key': 'value'}")
                .template("Test Template")
                .sqlStr("SELECT * FROM test")
                .sqlPart("WHERE id = 1")
                .build();
    }

    @Test
    @DisplayName("Should save RuleName successfully")
    void save_WhenRuleNameValid_ShouldSaveRuleName() {
        // Given
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(testRuleName);

        // When
        RuleName savedRuleName = ruleNameService.save(testRuleName);

        // Then
        assertNotNull(savedRuleName);
        assertEquals(testRuleName.getName(), savedRuleName.getName());
        assertEquals(testRuleName.getDescription(), savedRuleName.getDescription());
        assertEquals(testRuleName.getJson(), savedRuleName.getJson());
        verify(ruleNameRepository).save(testRuleName);
    }

    @Test
    @DisplayName("Should find all RuleNames")
    void findAll_ShouldReturnAllRuleNames() {
        // Given
        RuleName anotherRuleName = RuleName.builder()
                .id(2)
                .name("Another Rule")
                .description("Another Description")
                .json("{'another': 'json'}")
                .template("Another Template")
                .build();
        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(testRuleName, anotherRuleName));

        // When
        Iterable<RuleName> result = ruleNameService.findAll();

        // Then
        assertNotNull(result);
        verify(ruleNameRepository).findAll();
    }

    @Test
    @DisplayName("Should find RuleName by ID")
    void findById_WhenRuleNameExists_ShouldReturnRuleName() {
        // Given
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(testRuleName));

        // When
        Optional<RuleName> result = ruleNameService.findById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testRuleName, result.get());
        verify(ruleNameRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when RuleName not found")
    void findById_WhenRuleNameNotExists_ShouldReturnEmpty() {
        // Given
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        // When
        Optional<RuleName> result = ruleNameService.findById(99);

        // Then
        assertFalse(result.isPresent());
        verify(ruleNameRepository).findById(99);
    }

    @Test
    @DisplayName("Should delete RuleName by ID")
    void deleteById_WhenIdValid_ShouldDeleteRuleName() {
        // Given
        Integer ruleNameId = 1;

        // When
        ruleNameService.deleteById(ruleNameId);

        // Then
        verify(ruleNameRepository).deleteById(ruleNameId);
    }

    @Test
    @DisplayName("Should check if RuleName exists by ID")
    void existsById_WhenRuleNameExists_ShouldReturnTrue() {
        // Given
        when(ruleNameRepository.existsById(1)).thenReturn(true);

        // When
        boolean exists = ruleNameService.existsById(1);

        // Then
        assertTrue(exists);
        verify(ruleNameRepository).existsById(1);
    }

    @Test
    @DisplayName("Should handle RuleName with complex JSON configuration")
    void save_WhenRuleNameHasComplexJson_ShouldSaveSuccessfully() {
        // Given
        RuleName complexRule = RuleName.builder()
                .name("Complex Rule")
                .description("Rule with complex JSON")
                .json("{'validation': {'required': true, 'min': 0, 'max': 100}, 'formatting': {'decimal': 2}}")
                .template("Complex Template")
                .sqlStr("SELECT * FROM complex_table")
                .build();
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(complexRule);

        // When
        RuleName savedRule = ruleNameService.save(complexRule);

        // Then
        assertNotNull(savedRule);
        assertEquals("Complex Rule", savedRule.getName());
        assertTrue(savedRule.getJson().contains("validation"));
        verify(ruleNameRepository).save(complexRule);
    }
}
