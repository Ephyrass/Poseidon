package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing RuleName entities.
 * Provides CRUD operations delegating to the RuleNameRepository.
 */
@Service
public class RuleNameService {
    private final RuleNameRepository ruleNameRepository;

    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    /**
     * Persist a RuleName entity.
     *
     * @param ruleName the entity to save; must not be null
     * @return the saved RuleName instance
     */
    public RuleName save(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    /**
     * Find a RuleName by its identifier.
     *
     * @param id the id of the RuleName to find
     * @return an Optional containing the RuleName if found, otherwise empty
     */
    public Optional<RuleName> findById(Integer id) {
        return ruleNameRepository.findById(id);
    }

    /**
     * Retrieve all RuleName entities.
     *
     * @return an Iterable containing all RuleName instances
     */
    public Iterable<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    /**
     * Delete a RuleName by its identifier.
     *
     * @param id the id of the RuleName to delete
     */
    public void deleteById(Integer id) {
        ruleNameRepository.deleteById(id);
    }

    /**
     * Check whether a RuleName exists by id.
     *
     * @param id the id to check
     * @return true if a RuleName exists with the given id, false otherwise
     */
    public boolean existsById(Integer id) {
        return ruleNameRepository.existsById(id);
    }
}
