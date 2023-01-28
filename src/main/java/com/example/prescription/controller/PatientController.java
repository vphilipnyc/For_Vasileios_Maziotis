package com.example.prescription.controller;

import com.example.prescription.model.Drug;
import com.example.prescription.model.Patient;
import com.example.prescription.service.DrugService;
import com.example.prescription.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@Slf4j
public class PatientController {

    private final PatientService patientService;
    private final DrugService drugService;

    public PatientController(PatientService patientService,
                             DrugService drugService) {
        this.patientService = patientService;
        this.drugService = drugService;
    }

    @GetMapping("/patients")
    public String getAllPatients(Model model) {
        model.addAttribute("patientList", patientService.findAll());
        return "patients";
    }

    @GetMapping("/newPatient")
    public String register(Model model) {
        Patient patient = Patient.builder()
                .firstName("Vasileios")
                .lastName("Maziotis")
                .build();
        model.addAttribute("patient", patient);
        model.addAttribute("drugs", drugService.findAll());
        return "patientForm";
    }

    @PostMapping("/patient/save")
    public String savePatient(Patient patient) {
        patientService.save(patient);
        return "redirect:/allPatients";
    }

    @GetMapping("/editPatient/{id}")
    public String editPatient(@PathVariable(value = "id") Long id,
                              Model model) {
        model.addAttribute("patient", patientService.findById(id));
        return "patientFormEditToUpdate";
    }

    @PostMapping("/updatePatient/{id}")
    public String updatePatient(@PathVariable(value = "id") Long id) {
        Optional<Patient> patientOptional = patientService.findById(id);
        patientOptional.ifPresent(patientService::save);
        return "redirect:/allPatients";
    }

    @GetMapping("/deletePatient/{id}")
    public String deleteById(@PathVariable(value = "id") Long id) {
        Optional<Patient> patientOptional = patientService.findById(id);
        patientOptional.ifPresent(patientService::delete);
        return "redirect:/allPatients";
    }

    @GetMapping("/prescribeDrugs/{patientId}")
    public String prescribeDrugs(@PathVariable("patientId") Long id, Model model) {
        Optional<Patient> patient = patientService.findById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("drugs", drugService.findAll());
        return "patientFormEdit";
    }

    @PostMapping("/prescribeDrugs/Patient/{patientId}")
    public String prescribePatientDrugs(@Valid Patient patient,
                                        @PathVariable(value = "patientId") Long patientId,
                                        @ModelAttribute(value = "drugs") Long drugId,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return "patients";
        }
        try {
            Optional<Patient> formPatient = patientService.findById(patientId);

            formPatient.setCity(patient.getCity());
            formPatient.setEmail(patient.getEmail());
            formPatient.setPhone(patient.getPhone());
            formPatient.setSymptoms(patient.getSymptoms());
            formPatient.setPharmacy(patient.getPharmacy());
            formPatient.setDoctorsName(patient.getDoctorsName());
            formPatient.setMessage(patient.getMessage());

            Optional<Drug> drug = drugService.findById(drugId);
            Drug patientDrug = new Drug(patient, drug, LocalDate.now());
            drugService.save(drug);
            formPatient.getDrugs().add(patientDrug);
            patientService.save(formPatient);
        } catch (NumberFormatException numberFormatException) {
            log.error("NumberFormatException: " + numberFormatException);
        }
        return "redirect:/allPatients";
    }
}
