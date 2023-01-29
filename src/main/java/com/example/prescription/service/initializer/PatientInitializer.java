package com.example.prescription.service.initializer;

import com.example.prescription.model.Patient;
import com.example.prescription.repository.DrugRepository;
import com.example.prescription.repository.PatientRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@Order(2)
public class PatientInitializer implements ApplicationRunner {
    private final PatientRepository patientRepository;
    private final DrugRepository drugRepository;

    public PatientInitializer(PatientRepository patientRepository,
                              DrugRepository drugRepository) {
        this.patientRepository = patientRepository;
        this.drugRepository = drugRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Patient john = Patient.builder()
                .firstName("John")
                .lastName("Smith")
                .email("smith@smith.com")
                .amka("12323231313-2223")
                .phone("(212) 222-2222")
                .city("San Diego")
                .build();
        john.setDrugs(Set.of(Objects.requireNonNull(drugRepository.findById(1L).orElse(null))));
        Patient mary = Patient.builder()
                .firstName("Mary")
                .lastName("Kotsikopoulos")
                .email("mary@mary.com")
                .amka("92323234413-2223")
                .phone("(223) 222-2222")
                .city("Athens")
                .build();
        mary.setDrugs(Set.of(Objects.requireNonNull(drugRepository.findById(2L).orElse(null))));
        Patient raj = Patient.builder()
                .firstName("Raj")
                .lastName("Kapoor")
                .email("raj@raj.com")
                .amka("88323231313-2223")
                .phone("(124) 222-2222")
                .city("New Delhi")
                .build();
        raj.setDrugs(Set.of(Objects.requireNonNull(drugRepository.findById(3L).orElse(null))));
        Patient candice = Patient.builder()
                .firstName("Candice")
                .lastName("O'Malley")
                .email("candy@candy.com")
                .amka("32143455-2223")
                .phone("(555) 555-5553")
                .city("Dublin")
                .build();
        candice.setDrugs(Set.of(Objects.requireNonNull(drugRepository.findById(3L).orElse(null))));

        patientRepository.saveAll(Set.of(john, mary, raj, candice));
    }
}
