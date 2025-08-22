package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
 * Unit tests for CurvePointService.
 * Tests all CRUD operations and business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CurvePointService Tests")
class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
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
    @DisplayName("Should save CurvePoint successfully")
    void save_WhenCurvePointValid_ShouldSaveCurvePoint() {
        // Given
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(testCurvePoint);

        // When
        CurvePoint savedCurvePoint = curvePointService.save(testCurvePoint);

        // Then
        assertNotNull(savedCurvePoint);
        assertEquals(testCurvePoint.getCurveId(), savedCurvePoint.getCurveId());
        assertEquals(testCurvePoint.getTerm(), savedCurvePoint.getTerm());
        assertEquals(testCurvePoint.getValue(), savedCurvePoint.getValue());
        verify(curvePointRepository).save(testCurvePoint);
    }

    @Test
    @DisplayName("Should find all CurvePoints")
    void findAll_ShouldReturnAllCurvePoints() {
        // Given
        CurvePoint anotherCurvePoint = CurvePoint.builder()
                .id(2)
                .curveId(2)
                .term(15.0)
                .value(35.0)
                .build();
        when(curvePointRepository.findAll()).thenReturn(Arrays.asList(testCurvePoint, anotherCurvePoint));

        // When
        Iterable<CurvePoint> result = curvePointService.findAll();

        // Then
        assertNotNull(result);
        verify(curvePointRepository).findAll();
    }

    @Test
    @DisplayName("Should find CurvePoint by ID")
    void findById_WhenCurvePointExists_ShouldReturnCurvePoint() {
        // Given
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(testCurvePoint));

        // When
        Optional<CurvePoint> result = curvePointService.findById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testCurvePoint, result.get());
        verify(curvePointRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when CurvePoint not found")
    void findById_WhenCurvePointNotExists_ShouldReturnEmpty() {
        // Given
        when(curvePointRepository.findById(99)).thenReturn(Optional.empty());

        // When
        Optional<CurvePoint> result = curvePointService.findById(99);

        // Then
        assertFalse(result.isPresent());
        verify(curvePointRepository).findById(99);
    }

    @Test
    @DisplayName("Should delete CurvePoint by ID")
    void deleteById_WhenIdValid_ShouldDeleteCurvePoint() {
        // Given
        Integer curvePointId = 1;

        // When
        curvePointService.deleteById(curvePointId);

        // Then
        verify(curvePointRepository).deleteById(curvePointId);
    }

    @Test
    @DisplayName("Should check if CurvePoint exists by ID")
    void existsById_WhenCurvePointExists_ShouldReturnTrue() {
        // Given
        when(curvePointRepository.existsById(1)).thenReturn(true);

        // When
        boolean exists = curvePointService.existsById(1);

        // Then
        assertTrue(exists);
        verify(curvePointRepository).existsById(1);
    }
}
