package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for RuleName entity and repository.
 * Tests CRUD operations and business logic validation.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RuleTests {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    private RuleName testRule;

    @BeforeEach
    void setUp() {
        testRule = RuleName.builder()
                .name("Test Rule")
                .description("Test Description")
                .json("{'key': 'value'}")
                .template("Test Template")
                .sqlStr("SELECT * FROM test")
                .sqlPart("WHERE id = 1")
                .build();
    }

    @Test
    @DisplayName("Should save and retrieve RuleName successfully")
    public void testSaveRuleName() {
        // Given
        assertNull(testRule.getId());

        // When
        RuleName savedRule = ruleNameRepository.save(testRule);

        // Then
        assertNotNull(savedRule.getId());
        assertEquals("Test Rule", savedRule.getName());
        assertEquals("Test Description", savedRule.getDescription());
        assertEquals("{'key': 'value'}", savedRule.getJson());
        assertEquals("Test Template", savedRule.getTemplate());
    }

    @Test
    @DisplayName("Should update RuleName successfully")
    public void testUpdateRuleName() {
        // Given
        RuleName savedRule = ruleNameRepository.save(testRule);

        // When
        savedRule.setName("Updated Rule");
        savedRule.setDescription("Updated Description");
        RuleName updatedRule = ruleNameRepository.save(savedRule);

        // Then
        assertEquals("Updated Rule", updatedRule.getName());
        assertEquals("Updated Description", updatedRule.getDescription());
        assertEquals(savedRule.getId(), updatedRule.getId());
    }

    @Test
    @DisplayName("Should find RuleName by ID")
    public void testFindRuleNameById() {
        // Given
        RuleName savedRule = ruleNameRepository.save(testRule);

        // When
        Optional<RuleName> foundRule = ruleNameRepository.findById(savedRule.getId());

        // Then
        assertTrue(foundRule.isPresent());
        assertEquals(savedRule.getName(), foundRule.get().getName());
        assertEquals(savedRule.getDescription(), foundRule.get().getDescription());
    }

    @Test
    @DisplayName("Should delete RuleName successfully")
    public void testDeleteRuleName() {
        // Given
        RuleName savedRule = ruleNameRepository.save(testRule);
        Integer ruleId = savedRule.getId();

        // When
        ruleNameRepository.delete(savedRule);

        // Then
        Optional<RuleName> deletedRule = ruleNameRepository.findById(ruleId);
        assertFalse(deletedRule.isPresent());
    }

    @Test
    @DisplayName("Should find all RuleNames")
    public void testFindAllRuleNames() {
        // Given
        ruleNameRepository.save(testRule);
        RuleName anotherRule = RuleName.builder()
                .name("Another Rule")
                .description("Another Description")
                .json("{'another': 'json'}")
                .template("Another Template")
                .build();
        ruleNameRepository.save(anotherRule);

        // When
        Iterable<RuleName> allRules = ruleNameRepository.findAll();

        // Then
        assertNotNull(allRules);
        assertTrue(allRules.iterator().hasNext());

        long count = 0;
        for (RuleName rule : allRules) {
            count++;
        }
        assertTrue(count >= 2);
    }

    @Test
    @DisplayName("Should handle RuleName with null values appropriately")
    public void testRuleNameWithNullValues() {
        // Given
        RuleName ruleWithNulls = RuleName.builder()
                .name("Minimal Rule")
                // other fields are null
                .build();

        // When & Then
        assertDoesNotThrow(() -> {
            RuleName savedRule = ruleNameRepository.save(ruleWithNulls);
            assertNotNull(savedRule.getId());
        });
    }

    @Test
    @DisplayName("Should count RuleNames correctly")
    public void testCountRuleNames() {
        // Given
        long initialCount = ruleNameRepository.count();
        ruleNameRepository.save(testRule);

        // When
        long newCount = ruleNameRepository.count();

        // Then
        assertEquals(initialCount + 1, newCount);
    }
}
