package com.example.prescription.service.initializer;

import com.example.prescription.model.Drug;
import com.example.prescription.repository.DrugRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Order(1)
public class DrugInitializer implements ApplicationRunner {
    private final DrugRepository drugRepository;

    public DrugInitializer(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Drug depon = Drug.builder()
                .name("depon")
                .build();
        Drug aspirin = Drug.builder()
                .name("aspirin")
                .build();
        Drug panadol = Drug.builder()
                .name("panadol")
                .build();
        drugRepository.saveAll(Set.of(depon, aspirin, panadol));
    }
}
