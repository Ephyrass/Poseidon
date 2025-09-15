package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing CurvePoint entities.
 * Provides CRUD operations delegating to the CurvePointRepository.
 */
@Service
public class CurvePointService {
    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    /**
     * Persist a CurvePoint entity.
     *
     * @param curvePoint the entity to save; must not be null
     * @return the saved CurvePoint instance
     */
    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    /**
     * Find a CurvePoint by its identifier.
     *
     * @param id the id of the CurvePoint to find
     * @return an Optional containing the CurvePoint if found, otherwise empty
     */
    public Optional<CurvePoint> findById(Integer id) {
        return curvePointRepository.findById(id);
    }

    /**
     * Retrieve all CurvePoint entities.
     *
     * @return an Iterable containing all CurvePoint instances
     */
    public Iterable<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    /**
     * Delete a CurvePoint by its identifier.
     *
     * @param id the id of the CurvePoint to delete
     */
    public void deleteById(Integer id) {
        curvePointRepository.deleteById(id);
    }

    /**
     * Check whether a CurvePoint exists by id.
     *
     * @param id the id to check
     * @return true if a CurvePoint exists with the given id, false otherwise
     */
    public boolean existsById(Integer id) {
        return curvePointRepository.existsById(id);
    }
}
