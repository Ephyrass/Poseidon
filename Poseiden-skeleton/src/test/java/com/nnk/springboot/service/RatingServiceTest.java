package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
 * Unit tests for RatingService.
 * Tests all CRUD operations and business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RatingService Tests")
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
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
    @DisplayName("Should save Rating successfully")
    void save_WhenRatingValid_ShouldSaveRating() {
        // Given
        when(ratingRepository.save(any(Rating.class))).thenReturn(testRating);

        // When
        Rating savedRating = ratingService.save(testRating);

        // Then
        assertNotNull(savedRating);
        assertEquals(testRating.getMoodysRating(), savedRating.getMoodysRating());
        assertEquals(testRating.getSandPRating(), savedRating.getSandPRating());
        assertEquals(testRating.getFitchRating(), savedRating.getFitchRating());
        verify(ratingRepository).save(testRating);
    }

    @Test
    @DisplayName("Should find all Ratings")
    void findAll_ShouldReturnAllRatings() {
        // Given
        Rating anotherRating = Rating.builder()
                .id(2)
                .moodysRating("Aa1")
                .sandPRating("AA+")
                .fitchRating("AA+")
                .orderNumber(2)
                .build();
        when(ratingRepository.findAll()).thenReturn(Arrays.asList(testRating, anotherRating));

        // When
        Iterable<Rating> result = ratingService.findAll();

        // Then
        assertNotNull(result);
        verify(ratingRepository).findAll();
    }

    @Test
    @DisplayName("Should find Rating by ID")
    void findById_WhenRatingExists_ShouldReturnRating() {
        // Given
        when(ratingRepository.findById(1)).thenReturn(Optional.of(testRating));

        // When
        Optional<Rating> result = ratingService.findById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testRating, result.get());
        verify(ratingRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when Rating not found")
    void findById_WhenRatingNotExists_ShouldReturnEmpty() {
        // Given
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        // When
        Optional<Rating> result = ratingService.findById(99);

        // Then
        assertFalse(result.isPresent());
        verify(ratingRepository).findById(99);
    }

    @Test
    @DisplayName("Should delete Rating by ID")
    void deleteById_WhenIdValid_ShouldDeleteRating() {
        // Given
        Integer ratingId = 1;

        // When
        ratingService.deleteById(ratingId);

        // Then
        verify(ratingRepository).deleteById(ratingId);
    }

    @Test
    @DisplayName("Should check if Rating exists by ID")
    void existsById_WhenRatingExists_ShouldReturnTrue() {
        // Given
        when(ratingRepository.existsById(1)).thenReturn(true);

        // When
        boolean exists = ratingService.existsById(1);

        // Then
        assertTrue(exists);
        verify(ratingRepository).existsById(1);
    }

    @Test
    @DisplayName("Should validate rating values")
    void save_WhenRatingHasValidValues_ShouldSaveSuccessfully() {
        // Given
        Rating validRating = Rating.builder()
                .moodysRating("Baa2")
                .sandPRating("BBB")
                .fitchRating("BBB-")
                .orderNumber(5)
                .build();
        when(ratingRepository.save(any(Rating.class))).thenReturn(validRating);

        // When
        Rating savedRating = ratingService.save(validRating);

        // Then
        assertNotNull(savedRating);
        assertEquals("Baa2", savedRating.getMoodysRating());
        assertEquals("BBB", savedRating.getSandPRating());
        assertEquals("BBB-", savedRating.getFitchRating());
        verify(ratingRepository).save(validRating);
    }
}
