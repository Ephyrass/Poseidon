package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing BidList entities.
 * Provides CRUD operations delegating to the BidListRepository.
 */
@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    /**
     * Persist a BidList entity.
     *
     * @param bidList the entity to save; must not be null
     * @return the saved BidList instance
     */
    public BidList save(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    /**
     * Find a BidList by its identifier.
     *
     * @param id the id of the BidList to find
     * @return an Optional containing the BidList if found, otherwise empty
     */
    public Optional<BidList> findById(Integer id) {
        return bidListRepository.findById(id);
    }

    /**
     * Delete a BidList by its identifier.
     *
     * @param id the id of the BidList to delete
     */
    public void deleteById(Integer id) {
        bidListRepository.deleteById(id);
    }

    /**
     * Retrieve all BidList entities.
     *
     * @return an Iterable containing all BidList instances
     */
    public Iterable<BidList> findAll() {
        return bidListRepository.findAll();
    }

    /**
     * Check whether a BidList exists by id.
     *
     * @param id the id to check
     * @return true if a BidList exists with the given id, false otherwise
     */
    public boolean existsById(Integer id) {
        return bidListRepository.existsById(id);
    }
}
