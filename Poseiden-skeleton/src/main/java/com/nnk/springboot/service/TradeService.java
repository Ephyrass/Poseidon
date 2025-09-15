package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing Trade entities.
 * Provides CRUD operations delegating to the TradeRepository.
 */
@Service
public class TradeService {
    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    /**
     * Persist a trade entity.
     *
     * @param trade the trade entity to save; must not be null
     * @return the saved Trade instance
     */
    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    /**
     * Find a trade by its identifier.
     *
     * @param id the id of the trade to find
     * @return an Optional containing the Trade if found, otherwise empty
     */
    public Optional<Trade> findById(Integer id) {
        return tradeRepository.findById(id);
    }

    /**
     * Retrieve all trades.
     *
     * @return an Iterable containing all Trade instances
     */
    public Iterable<Trade> findAll() {
        return tradeRepository.findAll();
    }

    /**
     * Delete a trade by its identifier.
     *
     * @param id the id of the trade to delete
     */
    public void deleteById(Integer id) {
        tradeRepository.deleteById(id);
    }

    /**
     * Check whether a trade exists by id.
     *
     * @param id the id to check
     * @return true if a trade exists with the given id, false otherwise
     */
    public boolean existsById(Integer id) {
        return tradeRepository.existsById(id);
    }
}
