package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Trade entity and repository.
 * Tests CRUD operations and business logic validation.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TradeTests {

    @Autowired
    private TradeRepository tradeRepository;

    private Trade testTrade;

    @BeforeEach
    void setUp() {
        testTrade = Trade.builder()
                .account("Test Account")
                .type("Test Type")
                .buyQuantity(100.0)
                .sellQuantity(50.0)
                .buyPrice(25.5)
                .sellPrice(26.0)
                .tradeDate(Timestamp.valueOf("2023-12-22"))
                .security("Test Security")
                .status("Open")
                .trader("Test Trader")
                .benchmark("Test Benchmark")
                .book("Test Book")
                .creationName("Test Creator")
                .side("Buy")
                .build();
    }

    @Test
    @DisplayName("Should save and retrieve Trade successfully")
    public void testSaveTrade() {
        // Given
        assertNull(testTrade.getTradeId());

        // When
        Trade savedTrade = tradeRepository.save(testTrade);

        // Then
        assertNotNull(savedTrade.getTradeId());
        assertEquals("Test Account", savedTrade.getAccount());
        assertEquals("Test Type", savedTrade.getType());
        assertEquals(100.0, savedTrade.getBuyQuantity());
        assertEquals(25.5, savedTrade.getBuyPrice());
    }

    @Test
    @DisplayName("Should update Trade successfully")
    public void testUpdateTrade() {
        // Given
        Trade savedTrade = tradeRepository.save(testTrade);

        // When
        savedTrade.setBuyQuantity(200.0);
        savedTrade.setStatus("Closed");
        Trade updatedTrade = tradeRepository.save(savedTrade);

        // Then
        assertEquals(200.0, updatedTrade.getBuyQuantity());
        assertEquals("Closed", updatedTrade.getStatus());
        assertEquals(savedTrade.getTradeId(), updatedTrade.getTradeId());
    }

    @Test
    @DisplayName("Should find Trade by ID")
    public void testFindTradeById() {
        // Given
        Trade savedTrade = tradeRepository.save(testTrade);

        // When
        Optional<Trade> foundTrade = tradeRepository.findById(savedTrade.getTradeId());

        // Then
        assertTrue(foundTrade.isPresent());
        assertEquals(savedTrade.getAccount(), foundTrade.get().getAccount());
        assertEquals(savedTrade.getType(), foundTrade.get().getType());
    }

    @Test
    @DisplayName("Should delete Trade successfully")
    public void testDeleteTrade() {
        // Given
        Trade savedTrade = tradeRepository.save(testTrade);
        Integer tradeId = savedTrade.getTradeId();

        // When
        tradeRepository.delete(savedTrade);

        // Then
        Optional<Trade> deletedTrade = tradeRepository.findById(tradeId);
        assertFalse(deletedTrade.isPresent());
    }

    @Test
    @DisplayName("Should find all Trades")
    public void testFindAllTrades() {
        // Given
        tradeRepository.save(testTrade);
        Trade anotherTrade = Trade.builder()
                .account("Another Account")
                .type("Another Type")
                .buyQuantity(75.0)
                .build();
        tradeRepository.save(anotherTrade);

        // When
        Iterable<Trade> allTrades = tradeRepository.findAll();

        // Then
        assertNotNull(allTrades);
        assertTrue(allTrades.iterator().hasNext());

        long count = 0;
        for (Trade trade : allTrades) {
            count++;
        }
        assertTrue(count >= 2);
    }

    @Test
    @DisplayName("Should handle Trade with null values appropriately")
    public void testTradeWithNullValues() {
        // Given
        Trade tradeWithNulls = Trade.builder()
                .account("Minimal Account")
                .type("Minimal Type")
                // other fields are null
                .build();

        // When & Then
        assertDoesNotThrow(() -> {
            Trade savedTrade = tradeRepository.save(tradeWithNulls);
            assertNotNull(savedTrade.getTradeId());
        });
    }

    @Test
    @DisplayName("Should count Trades correctly")
    public void testCountTrades() {
        // Given
        long initialCount = tradeRepository.count();
        tradeRepository.save(testTrade);

        // When
        long newCount = tradeRepository.count();

        // Then
        assertEquals(initialCount + 1, newCount);
    }
}
