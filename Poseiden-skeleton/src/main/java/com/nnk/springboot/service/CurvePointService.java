package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public Optional<CurvePoint> findById(Integer id) {
        return curvePointRepository.findById(id);
    }

    public Iterable<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public void deleteById(Integer id) {
        curvePointRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return curvePointRepository.existsById(id);
    }
}

