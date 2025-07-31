package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    public Optional<Trade> findById(Integer id) {
        return tradeRepository.findById(id);
    }

    public Iterable<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public void deleteById(Integer id) {
        tradeRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return tradeRepository.existsById(id);
    }
}
