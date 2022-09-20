package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.services.CertificateService;

@RestController
@RequestMapping("/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;


    @PostMapping
    public ResponseEntity<CertificateDTO> addCertificate(
            @RequestBody CertificateDTO certificateDTO){
        return ResponseEntity.ok(certificateService.save(certificateDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CertificateDTO> updateCertificate(
            @PathVariable Long id,
            @RequestBody CertificateDTO certificateDTO){
        return ResponseEntity.ok(certificateService.update(/*id,*/ certificateDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Long id){
        return certificateService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<CertificateDTO> getByID(@PathVariable Long id){
        return ResponseEntity.ok(certificateService.findById(id));
    }

}
