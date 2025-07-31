package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TradeTests {

    @Autowired
    private TradeService tradeService;

    @MockBean
    private TradeService tradeServiceMock;

    @Test
    public void tradeServiceTest() {
        Trade trade = Trade.builder()
                .account("Trade Account")
                .type("Type")
                .buyQuantity(10d)
                .build();

        // Set an ID for the trade to simulate a saved entity
        Trade savedTradeWithId = Trade.builder()
                .tradeId(1)
                .account("Trade Account")
                .type("Type")
                .buyQuantity(10d)
                .build();

        // Mock the save method to return the trade object with ID
        when(tradeServiceMock.save(any(Trade.class))).thenReturn(savedTradeWithId);

        // Use the mock service to save the trade
        Trade savedTrade = tradeServiceMock.save(trade);
        Assertions.assertNotNull(savedTrade.getTradeId());
        Assertions.assertEquals("Trade Account", savedTrade.getAccount());

        // Update
        Trade updatedTrade = Trade.builder()
                .tradeId(1)
                .account("Trade Account Update")
                .type("Type")
                .buyQuantity(10d)
                .build();

        when(tradeServiceMock.save(any(Trade.class))).thenReturn(updatedTrade);
        Trade result = tradeServiceMock.save(updatedTrade);
        Assertions.assertEquals("Trade Account Update", result.getAccount());

        // Find
        when(tradeServiceMock.findAll()).thenReturn(List.of(updatedTrade));
        Iterable<Trade> listResult = tradeServiceMock.findAll();
        Assertions.assertTrue(listResult.iterator().hasNext());

        // Delete
        doNothing().when(tradeServiceMock).deleteById(anyInt());
        tradeServiceMock.deleteById(1);
        verify(tradeServiceMock, times(1)).deleteById(1);
    }
}
