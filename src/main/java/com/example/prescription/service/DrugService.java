package com.example.prescription.service;

import com.example.prescription.model.Drug;
import com.example.prescription.repository.DrugRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DrugService {

    private final DrugRepository drugRepository;

    public DrugService(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    @Transactional(readOnly = true)
    public List<Drug> findAll() {
        return drugRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Drug> findById(Long id) {
        return drugRepository.findById(id);
    }
}
