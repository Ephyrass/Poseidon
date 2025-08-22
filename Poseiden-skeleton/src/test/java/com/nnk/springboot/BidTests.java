package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
 * Integration tests for BidList entity and repository.
 * Tests CRUD operations and business logic validation.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BidTests {

    @Autowired
    private BidListRepository bidListRepository;

    private BidList testBid;

    @BeforeEach
    void setUp() {
        testBid = BidList.builder()
                .account("Test Account")
                .type("Test Type")
                .bidQuantity(100.0)
                .build();
    }

    @Test
    @DisplayName("Should save and retrieve BidList successfully")
    public void testSaveBidList() {
        // Given
        assertNull(testBid.getId());

        // When
        BidList savedBid = bidListRepository.save(testBid);

        // Then
        assertNotNull(savedBid.getId());
        assertEquals("Test Account", savedBid.getAccount());
        assertEquals("Test Type", savedBid.getType());
        assertEquals(100.0, savedBid.getBidQuantity());
    }

    @Test
    @DisplayName("Should update BidList successfully")
    public void testUpdateBidList() {
        // Given
        BidList savedBid = bidListRepository.save(testBid);

        // When
        savedBid.setBidQuantity(200.0);
        savedBid.setAccount("Updated Account");
        BidList updatedBid = bidListRepository.save(savedBid);

        // Then
        assertEquals(200.0, updatedBid.getBidQuantity());
        assertEquals("Updated Account", updatedBid.getAccount());
        assertEquals(savedBid.getId(), updatedBid.getId());
    }

    @Test
    @DisplayName("Should find BidList by ID")
    public void testFindBidListById() {
        // Given
        BidList savedBid = bidListRepository.save(testBid);

        // When
        Optional<BidList> foundBid = bidListRepository.findById(savedBid.getId());

        // Then
        assertTrue(foundBid.isPresent());
        assertEquals(savedBid.getAccount(), foundBid.get().getAccount());
        assertEquals(savedBid.getType(), foundBid.get().getType());
    }

    @Test
    @DisplayName("Should delete BidList successfully")
    public void testDeleteBidList() {
        // Given
        BidList savedBid = bidListRepository.save(testBid);
        Integer bidId = savedBid.getId();

        // When
        bidListRepository.delete(savedBid);

        // Then
        Optional<BidList> deletedBid = bidListRepository.findById(bidId);
        assertFalse(deletedBid.isPresent());
    }

    @Test
    @DisplayName("Should find all BidLists")
    public void testFindAllBidLists() {
        // Given
        bidListRepository.save(testBid);
        BidList anotherBid = BidList.builder()
                .account("Another Account")
                .type("Another Type")
                .bidQuantity(50.0)
                .build();
        bidListRepository.save(anotherBid);

        // When
        Iterable<BidList> allBids = bidListRepository.findAll();

        // Then
        assertNotNull(allBids);
        assertTrue(allBids.iterator().hasNext());

        long count = 0;
        for (BidList bid : allBids) {
            count++;
        }
        assertTrue(count >= 2);
    }

    @Test
    @DisplayName("Should handle BidList with null values appropriately")
    public void testBidListWithNullValues() {
        // Given
        BidList bidWithNulls = BidList.builder()
                .account("Test Account")
                .type("Test Type")
                // bidQuantity is null
                .build();

        // When & Then
        assertDoesNotThrow(() -> {
            BidList savedBid = bidListRepository.save(bidWithNulls);
            assertNotNull(savedBid.getId());
        });
    }

    @Test
    @DisplayName("Should count BidLists correctly")
    public void testCountBidLists() {
        // Given
        long initialCount = bidListRepository.count();
        bidListRepository.save(testBid);

        // When
        long newCount = bidListRepository.count();

        // Then
        assertEquals(initialCount + 1, newCount);
    }
}
