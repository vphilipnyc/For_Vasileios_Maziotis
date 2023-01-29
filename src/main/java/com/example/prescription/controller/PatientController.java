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
import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
public class PatientController {
    private final PatientService patientService;
    private final DrugService drugService;

    //Leave @Autowired out.  We are using constructor injection.
    public PatientController(PatientService patientService,
                             DrugService drugService) {
        this.patientService = patientService;
        this.drugService = drugService;
    }

    //** THESE WORK.  Try the first one. **//
    @GetMapping("/patients")
    public String getAllPatients(Model model) {
        model.addAttribute("patientList", patientService.findAll());
        return "patients";
    }

    @GetMapping("/prescribeDrugs/{patientId}")
    public String prescribeDrugs(@PathVariable("patientId") Long id, Model model) {
        Optional<Patient> patientOptional = patientService.findById(id);
        patientOptional.ifPresent(patient -> model.addAttribute("patient", patient));
        model.addAttribute("allDrugs", drugService.findAll());
        return "patientFormEdit";
    }

    @PostMapping("/prescribed")
    public String prescriptionForm(@Valid @ModelAttribute("patient") Patient patient,
                                   BindingResult result) {
        if (result.hasErrors()) {
            log.debug("Page has errors");
            return "patientFormEdit";
        }

        //there is likely a better way to do this, but for the sake of speed:
        Optional<Patient> patientOptional = patientService.findById(patient.getId());
        patientOptional.ifPresent(patientFromRepo -> {
            patientFromRepo.setEmail(patient.getEmail());
            patientFromRepo.setPhone(patient.getPhone());
            patientFromRepo.setCity(patient.getCity());

            patientFromRepo.setSymptoms(patient.getSymptoms());
            Set<Drug> currentlyPrescribed = patientFromRepo.getDrugs();
            currentlyPrescribed.addAll(patient.getDrugs());
            patientFromRepo.setDrugs(currentlyPrescribed);

            patientFromRepo.setPharmacy(patient.getPharmacy());
            patientFromRepo.setDoctorsName(patient.getDoctorsName());
            patientFromRepo.setMessage(patient.getMessage());

            patientService.save(patientFromRepo);
        });
        return "redirect:/patients";
    }

    //** THESE ARE FOR YOU TO IMPLEMENT. **//
    //Make the naming consistent on your mappings!
    //Use either ModelAndView or just return the String.  It's confusing mixing them.
    //Spring will automatically convert objects of primitive types (like Long) as a method parameter.
    @GetMapping("/patient/create")
    public String register(Model model) {
        Patient patient = Patient.builder()
                .firstName("Vasileios")
                .lastName("Maziotis")
                .build(); //test person
        model.addAttribute("patient", patient);
        model.addAttribute("drugs", drugService.findAll());
        return "redirect:/patients";
    }

    @PostMapping("/patient/save")
    public String savePatient(Patient patient) {
        patientService.save(patient);
        return "redirect:/patients";
    }

    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable(value = "id") Long id) {
        Optional<Patient> patientOptional = patientService.findById(id);
        patientOptional.ifPresent(patientService::save);
        return "redirect:/patients";
    }

    @GetMapping("/patient/delete/{id}")
    public String deleteById(@PathVariable(value = "id") Long id) {
        Optional<Patient> patientOptional = patientService.findById(id);
        patientOptional.ifPresent(patientService::delete);
        return "redirect:/patients";
    }
}
