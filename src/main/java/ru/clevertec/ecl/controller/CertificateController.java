package ru.clevertec.ecl.controller;

import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.services.CertificateService;

@RestController
@RequestMapping("/v1/certificates")
@RequiredArgsConstructor
public class CertificateController {

  private final CertificateService certificateService;

  @PostMapping
  public ResponseEntity<CertificateDTO> addCertificate(@Valid @RequestBody CertificateDTO certificateDTO) {
    return new ResponseEntity<>(certificateService.save(certificateDTO), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CertificateDTO> updateCertificate(@PathVariable Long id,
                                                          @Valid @RequestBody CertificateDTO certificateDTO) {
    return ResponseEntity.ok(certificateService.update(id, certificateDTO));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable Long id) {
    certificateService.delete(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CertificateDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(certificateService.getById(id));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<CertificateDTO> getByName(@PathVariable String name) {
    return ResponseEntity.ok(certificateService.getByName(name));
  }

  @GetMapping
  public ResponseEntity<List<CertificateDTO>> getCertificateByNameOrDescription(
      @PageableDefault Pageable pageable,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String description) {
    return ResponseEntity.ok(certificateService.getByNameDescription(pageable, name, description));
  }

  @GetMapping("/tags/{tagsNames}")
  public ResponseEntity<Set<CertificateDTO>> getCertificateByTagsName(@PathVariable List<String> tagsNames,
                                                                      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(certificateService.getByTagsName(tagsNames, pageable));
  }
}
