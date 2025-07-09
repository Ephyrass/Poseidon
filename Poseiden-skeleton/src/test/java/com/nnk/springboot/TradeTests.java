package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TradeTests {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void tradeTest() {
        Trade trade = Trade.builder()
                .account("Trade Account")
                .type("Type")
                .buyQuantity(10d)
                .build();

        // Save
        trade = tradeRepository.save(trade);
        Assertions.assertNotNull(trade.getTradeId());
        Assertions.assertEquals("Trade Account", trade.getAccount());

        // Update
        trade.setAccount("Trade Account Update");
        trade = tradeRepository.save(trade);
        Assertions.assertEquals("Trade Account Update", trade.getAccount());

        // Find
        Iterable<Trade> listResult = tradeRepository.findAll();
        Assertions.assertTrue(listResult.iterator().hasNext());

        // Delete
        tradeRepository.delete(trade);
        Assertions.assertFalse(tradeRepository.findById(trade.getTradeId()).isPresent());
    }
}
