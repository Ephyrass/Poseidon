package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Optional<Rating> findById(Integer id) {
        return ratingRepository.findById(id);
    }

    public Iterable<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public void deleteById(Integer id) {
        ratingRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return ratingRepository.existsById(id);
    }
}

