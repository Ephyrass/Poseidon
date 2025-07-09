package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class CurvePointTests {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    public void curvePointTest() {
        CurvePoint curvePoint = CurvePoint.builder()
                .curveId(10)
                .term(10d)
                .value(30d)
                .build();

        // Save
        curvePoint = curvePointRepository.save(curvePoint);
        Assertions.assertNotNull(curvePoint.getId());

        // Update
        curvePoint.setValue(20d);
        curvePoint = curvePointRepository.save(curvePoint);
        Assertions.assertEquals(20d, curvePoint.getValue());

        // Find
        Iterable<CurvePoint> listResult = curvePointRepository.findAll();
        Assertions.assertTrue(listResult.iterator().hasNext());

        // Delete
        curvePointRepository.delete(curvePoint);
        Assertions.assertFalse(curvePointRepository.findById(curvePoint.getId()).isPresent());
    }
}
