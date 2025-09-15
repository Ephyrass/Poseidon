package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing Rating entities.
 * Provides CRUD operations delegating to the RatingRepository.
 */
@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Persist a rating entity.
     *
     * @param rating the rating entity to save; must not be null
     * @return the saved Rating instance
     */
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    /**
     * Find a rating by its identifier.
     *
     * @param id the id of the rating to find
     * @return an Optional containing the Rating if found, otherwise empty
     */
    public Optional<Rating> findById(Integer id) {
        return ratingRepository.findById(id);
    }

    /**
     * Retrieve all ratings.
     *
     * @return an Iterable containing all Rating instances
     */
    public Iterable<Rating> findAll() {
        return ratingRepository.findAll();
    }

    /**
     * Delete a rating by its identifier.
     *
     * @param id the id of the rating to delete
     */
    public void deleteById(Integer id) {
        ratingRepository.deleteById(id);
    }

    /**
     * Check whether a rating exists by id.
     *
     * @param id the id to check
     * @return true if a rating exists with the given id, false otherwise
     */
    public boolean existsById(Integer id) {
        return ratingRepository.existsById(id);
    }
}
