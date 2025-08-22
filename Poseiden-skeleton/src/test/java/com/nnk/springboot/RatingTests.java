package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
 * Integration tests for Rating entity and repository.
 * Tests CRUD operations and business logic validation.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RatingTests {

    @Autowired
    private RatingRepository ratingRepository;

    private Rating testRating;

    @BeforeEach
    void setUp() {
        testRating = Rating.builder()
                .moodysRating("Aaa")
                .sandPRating("AAA")
                .fitchRating("AAA")
                .orderNumber(1)
                .build();
    }

    @Test
    @DisplayName("Should save and retrieve Rating successfully")
    public void testSaveRating() {
        // Given
        assertNull(testRating.getId());

        // When
        Rating savedRating = ratingRepository.save(testRating);

        // Then
        assertNotNull(savedRating.getId());
        assertEquals("Aaa", savedRating.getMoodysRating());
        assertEquals("AAA", savedRating.getSandPRating());
        assertEquals("AAA", savedRating.getFitchRating());
        assertEquals(1, savedRating.getOrderNumber());
    }

    @Test
    @DisplayName("Should update Rating successfully")
    public void testUpdateRating() {
        // Given
        Rating savedRating = ratingRepository.save(testRating);

        // When
        savedRating.setMoodysRating("Aa1");
        savedRating.setSandPRating("AA+");
        savedRating.setOrderNumber(2);
        Rating updatedRating = ratingRepository.save(savedRating);

        // Then
        assertEquals("Aa1", updatedRating.getMoodysRating());
        assertEquals("AA+", updatedRating.getSandPRating());
        assertEquals(2, updatedRating.getOrderNumber());
        assertEquals(savedRating.getId(), updatedRating.getId());
    }

    @Test
    @DisplayName("Should find Rating by ID")
    public void testFindRatingById() {
        // Given
        Rating savedRating = ratingRepository.save(testRating);

        // When
        Optional<Rating> foundRating = ratingRepository.findById(savedRating.getId());

        // Then
        assertTrue(foundRating.isPresent());
        assertEquals(savedRating.getMoodysRating(), foundRating.get().getMoodysRating());
        assertEquals(savedRating.getSandPRating(), foundRating.get().getSandPRating());
    }

    @Test
    @DisplayName("Should delete Rating successfully")
    public void testDeleteRating() {
        // Given
        Rating savedRating = ratingRepository.save(testRating);
        Integer ratingId = savedRating.getId();

        // When
        ratingRepository.delete(savedRating);

        // Then
        Optional<Rating> deletedRating = ratingRepository.findById(ratingId);
        assertFalse(deletedRating.isPresent());
    }

    @Test
    @DisplayName("Should find all Ratings")
    public void testFindAllRatings() {
        // Given
        ratingRepository.save(testRating);
        Rating anotherRating = Rating.builder()
                .moodysRating("Baa1")
                .sandPRating("BBB+")
                .fitchRating("BBB+")
                .orderNumber(2)
                .build();
        ratingRepository.save(anotherRating);

        // When
        Iterable<Rating> allRatings = ratingRepository.findAll();

        // Then
        assertNotNull(allRatings);
        assertTrue(allRatings.iterator().hasNext());

        long count = 0;
        for (Rating rating : allRatings) {
            count++;
        }
        assertTrue(count >= 2);
    }

    @Test
    @DisplayName("Should handle Rating with null values appropriately")
    public void testRatingWithNullValues() {
        // Given
        Rating ratingWithNulls = Rating.builder()
                .moodysRating("Aaa")
                // other fields are null
                .build();

        // When & Then
        assertDoesNotThrow(() -> {
            Rating savedRating = ratingRepository.save(ratingWithNulls);
            assertNotNull(savedRating.getId());
        });
    }

    @Test
    @DisplayName("Should count Ratings correctly")
    public void testCountRatings() {
        // Given
        long initialCount = ratingRepository.count();
        ratingRepository.save(testRating);

        // When
        long newCount = ratingRepository.count();

        // Then
        assertEquals(initialCount + 1, newCount);
    }
}
