package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dmain.dto.CertificateDTO;
import ru.clevertec.ecl.services.CertificateService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificate")
@RequiredArgsConstructor
@Validated
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping
    public ResponseEntity<CertificateDTO> addCertificate(
            @Valid @RequestBody CertificateDTO certificateDTO) {
        return ResponseEntity.ok(certificateService.save(certificateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateDTO> updateCertificate(
            @PathVariable Long id,
            @RequestBody CertificateDTO certificateDTO) {
        return ResponseEntity.ok(certificateService.update(id, certificateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Long id) {
        return certificateService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateService.findById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<CertificateDTO> getByName(@PathVariable String name) {
        return ResponseEntity.ok(certificateService.findByName(name));
    }

    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getAllCertificate(@PageableDefault(value = 20) Pageable pageable) {
        return ResponseEntity.ok(certificateService.findAll(pageable));
    }

    @GetMapping("/tag")
    public ResponseEntity<List<CertificateDTO>> getCertificateTagNameOrDescription(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "name,desc") String[] sort) {

        return ResponseEntity.ok(certificateService.findByTagOrDescription(pageable, tagName, description, sort));
    }

}
