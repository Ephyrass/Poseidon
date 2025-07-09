package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;

    public RuleName save(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public Optional<RuleName> findById(Integer id) {
        return ruleNameRepository.findById(id);
    }

    public Iterable<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    public void deleteById(Integer id) {
        ruleNameRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return ruleNameRepository.existsById(id);
    }
}

