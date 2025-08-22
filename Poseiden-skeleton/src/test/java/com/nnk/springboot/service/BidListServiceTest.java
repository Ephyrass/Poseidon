package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
 * Unit tests for BidListService.
 * Tests all CRUD operations and business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BidListService Tests")
class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    private BidList testBidList;

    @BeforeEach
    void setUp() {
        testBidList = BidList.builder()
                .id(1)
                .account("Test Account")
                .type("Test Type")
                .bidQuantity(100.0)
                .build();
    }

    @Test
    @DisplayName("Should save BidList successfully")
    void save_WhenBidListValid_ShouldSaveBidList() {
        // Given
        when(bidListRepository.save(any(BidList.class))).thenReturn(testBidList);

        // When
        BidList savedBidList = bidListService.save(testBidList);

        // Then
        assertNotNull(savedBidList);
        assertEquals(testBidList.getAccount(), savedBidList.getAccount());
        assertEquals(testBidList.getType(), savedBidList.getType());
        verify(bidListRepository).save(testBidList);
    }

    @Test
    @DisplayName("Should find all BidLists")
    void findAll_ShouldReturnAllBidLists() {
        // Given
        BidList anotherBidList = BidList.builder()
                .id(2)
                .account("Another Account")
                .type("Another Type")
                .bidQuantity(200.0)
                .build();
        when(bidListRepository.findAll()).thenReturn(Arrays.asList(testBidList, anotherBidList));

        // When
        Iterable<BidList> result = bidListService.findAll();

        // Then
        assertNotNull(result);
        verify(bidListRepository).findAll();
    }

    @Test
    @DisplayName("Should find BidList by ID")
    void findById_WhenBidListExists_ShouldReturnBidList() {
        // Given
        when(bidListRepository.findById(1)).thenReturn(Optional.of(testBidList));

        // When
        Optional<BidList> result = bidListService.findById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testBidList, result.get());
        verify(bidListRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when BidList not found")
    void findById_WhenBidListNotExists_ShouldReturnEmpty() {
        // Given
        when(bidListRepository.findById(99)).thenReturn(Optional.empty());

        // When
        Optional<BidList> result = bidListService.findById(99);

        // Then
        assertFalse(result.isPresent());
        verify(bidListRepository).findById(99);
    }

    @Test
    @DisplayName("Should delete BidList by ID")
    void deleteById_WhenIdValid_ShouldDeleteBidList() {
        // Given
        Integer bidListId = 1;

        // When
        bidListService.deleteById(bidListId);

        // Then
        verify(bidListRepository).deleteById(bidListId);
    }

    @Test
    @DisplayName("Should check if BidList exists by ID")
    void existsById_WhenBidListExists_ShouldReturnTrue() {
        // Given
        when(bidListRepository.existsById(1)).thenReturn(true);

        // When
        boolean exists = bidListService.existsById(1);

        // Then
        assertTrue(exists);
        verify(bidListRepository).existsById(1);
    }

    @Test
    @DisplayName("Should return false when BidList does not exist")
    void existsById_WhenBidListNotExists_ShouldReturnFalse() {
        // Given
        when(bidListRepository.existsById(99)).thenReturn(false);

        // When
        boolean exists = bidListService.existsById(99);

        // Then
        assertFalse(exists);
        verify(bidListRepository).existsById(99);
    }
}
