package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.services.CertificateService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
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
            @Valid @RequestBody CertificateDTO certificateDTO) {
        return ResponseEntity.ok(certificateService.update(id, certificateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Long id) {
        return certificateService.delete(id)
                ? ResponseEntity.ok("delete certificate with id = " + id)
                : ResponseEntity.notFound().build();
    }

    @GetMapping(params = "id")
    public ResponseEntity<CertificateDTO> getById(
            @RequestParam(required = false) Long id) {
        return ResponseEntity.ok(certificateService.findById(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<CertificateDTO> getByName(@RequestParam String name) {
        return ResponseEntity.ok(certificateService.findByName(name));
    }

    @GetMapping()
    public ResponseEntity<List<CertificateDTO>> getCertificateByTagNameOrDescription(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "name,desc") String[] sort) {
        return ResponseEntity.ok(certificateService.findByTagOrDescription(pageable, tagName, description, sort));
    }
}
