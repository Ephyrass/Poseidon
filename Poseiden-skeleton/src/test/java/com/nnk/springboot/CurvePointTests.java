package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
 * Integration tests for CurvePoint entity and repository.
 * Tests CRUD operations and business logic validation.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CurvePointTests {

    @Autowired
    private CurvePointRepository curvePointRepository;

    private CurvePoint testCurvePoint;

    @BeforeEach
    void setUp() {
        testCurvePoint = CurvePoint.builder()
                .curveId(1)
                .term(10.0)
                .value(30.0)
                .build();
    }

    @Test
    @DisplayName("Should save and retrieve CurvePoint successfully")
    public void testSaveCurvePoint() {
        // Given
        assertNull(testCurvePoint.getId());

        // When
        CurvePoint savedCurvePoint = curvePointRepository.save(testCurvePoint);

        // Then
        assertNotNull(savedCurvePoint.getId());
        assertEquals(1, savedCurvePoint.getCurveId());
        assertEquals(10.0, savedCurvePoint.getTerm());
        assertEquals(30.0, savedCurvePoint.getValue());
    }

    @Test
    @DisplayName("Should update CurvePoint successfully")
    public void testUpdateCurvePoint() {
        // Given
        CurvePoint savedCurvePoint = curvePointRepository.save(testCurvePoint);

        // When
        savedCurvePoint.setTerm(20.0);
        savedCurvePoint.setValue(40.0);
        CurvePoint updatedCurvePoint = curvePointRepository.save(savedCurvePoint);

        // Then
        assertEquals(20.0, updatedCurvePoint.getTerm());
        assertEquals(40.0, updatedCurvePoint.getValue());
        assertEquals(savedCurvePoint.getId(), updatedCurvePoint.getId());
    }

    @Test
    @DisplayName("Should find CurvePoint by ID")
    public void testFindCurvePointById() {
        // Given
        CurvePoint savedCurvePoint = curvePointRepository.save(testCurvePoint);

        // When
        Optional<CurvePoint> foundCurvePoint = curvePointRepository.findById(savedCurvePoint.getId());

        // Then
        assertTrue(foundCurvePoint.isPresent());
        assertEquals(savedCurvePoint.getCurveId(), foundCurvePoint.get().getCurveId());
        assertEquals(savedCurvePoint.getTerm(), foundCurvePoint.get().getTerm());
    }

    @Test
    @DisplayName("Should delete CurvePoint successfully")
    public void testDeleteCurvePoint() {
        // Given
        CurvePoint savedCurvePoint = curvePointRepository.save(testCurvePoint);
        Integer curvePointId = savedCurvePoint.getId();

        // When
        curvePointRepository.delete(savedCurvePoint);

        // Then
        Optional<CurvePoint> deletedCurvePoint = curvePointRepository.findById(curvePointId);
        assertFalse(deletedCurvePoint.isPresent());
    }

    @Test
    @DisplayName("Should find all CurvePoints")
    public void testFindAllCurvePoints() {
        // Given
        curvePointRepository.save(testCurvePoint);
        CurvePoint anotherCurvePoint = CurvePoint.builder()
                .curveId(2)
                .term(15.0)
                .value(35.0)
                .build();
        curvePointRepository.save(anotherCurvePoint);

        // When
        Iterable<CurvePoint> allCurvePoints = curvePointRepository.findAll();

        // Then
        assertNotNull(allCurvePoints);
        assertTrue(allCurvePoints.iterator().hasNext());

        long count = 0;
        for (CurvePoint curvePoint : allCurvePoints) {
            count++;
        }
        assertTrue(count >= 2);
    }

    @Test
    @DisplayName("Should handle CurvePoint with null values appropriately")
    public void testCurvePointWithNullValues() {
        // Given
        CurvePoint curvePointWithNulls = CurvePoint.builder()
                .curveId(1)
                // term and value are null
                .build();

        // When & Then
        assertDoesNotThrow(() -> {
            CurvePoint savedCurvePoint = curvePointRepository.save(curvePointWithNulls);
            assertNotNull(savedCurvePoint.getId());
        });
    }

    @Test
    @DisplayName("Should count CurvePoints correctly")
    public void testCountCurvePoints() {
        // Given
        long initialCount = curvePointRepository.count();
        curvePointRepository.save(testCurvePoint);

        // When
        long newCount = curvePointRepository.count();

        // Then
        assertEquals(initialCount + 1, newCount);
    }
}
