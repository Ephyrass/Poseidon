package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TradeService.
 * Tests all CRUD operations and business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TradeService Tests")
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    private Trade testTrade;

    @BeforeEach
    void setUp() {
        testTrade = Trade.builder()
                .tradeId(1)
                .account("Test Account")
                .type("Test Type")
                .buyQuantity(100.0)
                .sellQuantity(50.0)
                .buyPrice(25.5)
                .sellPrice(26.0)
                .tradeDate(Timestamp.valueOf("2023-12-22 10:30:00"))
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
    @DisplayName("Should save Trade successfully")
    void save_WhenTradeValid_ShouldSaveTrade() {
        // Given
        when(tradeRepository.save(any(Trade.class))).thenReturn(testTrade);

        // When
        Trade savedTrade = tradeService.save(testTrade);

        // Then
        assertNotNull(savedTrade);
        assertEquals(testTrade.getAccount(), savedTrade.getAccount());
        assertEquals(testTrade.getType(), savedTrade.getType());
        assertEquals(testTrade.getBuyQuantity(), savedTrade.getBuyQuantity());
        assertEquals(testTrade.getBuyPrice(), savedTrade.getBuyPrice());
        verify(tradeRepository).save(testTrade);
    }

    @Test
    @DisplayName("Should delegate save to repository")
    void save_WhenCalled_ShouldDelegateToRepository() {
        // Given
        when(tradeRepository.save(testTrade)).thenReturn(testTrade);

        // When
        Trade result = tradeService.save(testTrade);

        // Then
        assertEquals(testTrade, result);
        verify(tradeRepository).save(testTrade);
    }

    @Test
    @DisplayName("Should find all Trades")
    void findAll_ShouldReturnAllTrades() {
        // Given
        Trade anotherTrade = Trade.builder()
                .tradeId(2)
                .account("Another Account")
                .type("Another Type")
                .buyQuantity(200.0)
                .tradeDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        when(tradeRepository.findAll()).thenReturn(Arrays.asList(testTrade, anotherTrade));

        // When
        Iterable<Trade> result = tradeService.findAll();

        // Then
        assertNotNull(result);
        verify(tradeRepository).findAll();
    }

    @Test
    @DisplayName("Should find Trade by ID")
    void findById_WhenTradeExists_ShouldReturnTrade() {
        // Given
        when(tradeRepository.findById(1)).thenReturn(Optional.of(testTrade));

        // When
        Optional<Trade> result = tradeService.findById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testTrade, result.get());
        verify(tradeRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when Trade not found")
    void findById_WhenTradeNotExists_ShouldReturnEmpty() {
        // Given
        when(tradeRepository.findById(99)).thenReturn(Optional.empty());

        // When
        Optional<Trade> result = tradeService.findById(99);

        // Then
        assertFalse(result.isPresent());
        verify(tradeRepository).findById(99);
    }

    @Test
    @DisplayName("Should delete Trade by ID")
    void deleteById_WhenIdValid_ShouldDeleteTrade() {
        // Given
        Integer tradeId = 1;

        // When
        tradeService.deleteById(tradeId);

        // Then
        verify(tradeRepository).deleteById(tradeId);
    }

    @Test
    @DisplayName("Should check if Trade exists by ID")
    void existsById_WhenTradeExists_ShouldReturnTrue() {
        // Given
        when(tradeRepository.existsById(1)).thenReturn(true);

        // When
        boolean exists = tradeService.existsById(1);

        // Then
        assertTrue(exists);
        verify(tradeRepository).existsById(1);
    }

    @Test
    @DisplayName("Should handle Trade with buy and sell operations")
    void save_WhenTradeHasBuyAndSellData_ShouldSaveSuccessfully() {
        // Given
        Trade buyAndSellTrade = Trade.builder()
                .account("Trading Account")
                .type("SWAP")
                .buyQuantity(1000.0)
                .sellQuantity(950.0)
                .buyPrice(100.50)
                .sellPrice(105.75)
                .tradeDate(Timestamp.valueOf("2024-01-15 14:30:00"))
                .security("BOND123")
                .status("Executed")
                .trader("John Trader")
                .side("Both")
                .build();
        when(tradeRepository.save(any(Trade.class))).thenReturn(buyAndSellTrade);

        // When
        Trade savedTrade = tradeService.save(buyAndSellTrade);

        // Then
        assertNotNull(savedTrade);
        assertEquals(1000.0, savedTrade.getBuyQuantity());
        assertEquals(950.0, savedTrade.getSellQuantity());
        assertEquals("SWAP", savedTrade.getType());
        assertEquals("Executed", savedTrade.getStatus());
        verify(tradeRepository).save(buyAndSellTrade);
    }

    @Test
    @DisplayName("Should validate trade date format")
    void save_WhenTradeHasValidDate_ShouldSaveSuccessfully() {
        // Given
        Timestamp testTimestamp = Timestamp.valueOf("2024-01-15 09:45:30");
        Trade tradeWithDate = Trade.builder()
                .account("Date Test Account")
                .type("BUY")
                .buyQuantity(500.0)
                .tradeDate(testTimestamp)
                .build();
        when(tradeRepository.save(any(Trade.class))).thenReturn(tradeWithDate);

        // When
        Trade savedTrade = tradeService.save(tradeWithDate);

        // Then
        assertNotNull(savedTrade);
        assertEquals(testTimestamp, savedTrade.getTradeDate());
        verify(tradeRepository).save(tradeWithDate);
    }

    @Test
    @DisplayName("Should handle Trade with current timestamp")
    void save_WhenTradeHasCurrentTimestamp_ShouldSaveSuccessfully() {
        // Given
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        Trade currentTrade = Trade.builder()
                .account("Current Account")
                .type("MARKET")
                .buyQuantity(750.0)
                .tradeDate(currentTime)
                .security("STOCK123")
                .status("Pending")
                .build();
        when(tradeRepository.save(any(Trade.class))).thenReturn(currentTrade);

        // When
        Trade savedTrade = tradeService.save(currentTrade);

        // Then
        assertNotNull(savedTrade);
        assertNotNull(savedTrade.getTradeDate());
        assertEquals("MARKET", savedTrade.getType());
        assertEquals("Pending", savedTrade.getStatus());
        verify(tradeRepository).save(currentTrade);
    }
}
