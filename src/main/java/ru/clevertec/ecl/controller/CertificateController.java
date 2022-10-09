package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.services.CertificateService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@Validated
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping
    public ResponseEntity<CertificateDTO> addCertificate(
            @Valid @RequestBody CertificateDTO certificateDTO) {
        return new ResponseEntity<>(certificateService.save(certificateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateDTO> updateCertificate(
            @PathVariable Long id,
            @Valid @RequestBody CertificateDTO certificateDTO) {
        return ResponseEntity.ok(certificateService.update(id, certificateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Long id) {
        certificateService.delete(id);
        return ResponseEntity.ok("Deleted certificate with id = " + id);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CertificateDTO> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(certificateService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CertificateDTO> getByName(@PathVariable String name) {
        return ResponseEntity.ok(certificateService.getByName(name));
    }

    @GetMapping()
    public ResponseEntity<List<CertificateDTO>> getCertificateByTagNameOrDescription(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String description) {
        return ResponseEntity.ok(certificateService.getByTagOrDescription(pageable, tagName, description));
    }

    @GetMapping("/tags/{tagsNames}")
    public ResponseEntity<Set<CertificateDTO>> getCertificateByTagsName(
            @PathVariable List<String> tagsNames,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(certificateService.getByTagsName(tagsNames, pageable));
    }

}
