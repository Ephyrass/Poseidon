package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    public BidList save(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    public Optional<BidList> findById(Integer id) {
        return bidListRepository.findById(id);
    }

    public void deleteById(Integer id) {
        bidListRepository.deleteById(id);
    }

    public Iterable<BidList> findAll() {
        return bidListRepository.findAll();
    }

    public boolean existsById(Integer id) {
        return bidListRepository.existsById(id);
    }
}
