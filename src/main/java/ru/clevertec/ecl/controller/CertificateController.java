package ru.clevertec.ecl.controller;

import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/v1/certificates")
@RequiredArgsConstructor
public class CertificateController {

  private final CertificateService certificateService;

  @PostMapping
  public ResponseEntity<CertificateDTO> addCertificate(@Valid @RequestBody CertificateDTO certificateDTO) {
    log.info("REQUEST: method = POST, path = /v1/certificates, body = {}", certificateDTO);
    CertificateDTO certificate = certificateService.save(certificateDTO);
    log.info("RESPONSE: responseBody = {}", certificate);
    return new ResponseEntity<>(certificate, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CertificateDTO> updateCertificate(@PathVariable Long id,
                                                          @Valid @RequestBody CertificateDTO certificateDTO) {
    log.info("REQUEST: method = PUT, path = /v1/certificates/{}, body = {}", id, certificateDTO);
    CertificateDTO certificate = certificateService.update(id, certificateDTO);
    log.info("RESPONSE: responseBody = {}", certificate);
    return ResponseEntity.ok(certificate);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable Long id) {
    log.info("REQUEST: method = DELETE, path = /v1/certificates/{}", id);
    certificateService.delete(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CertificateDTO> getById(@PathVariable Long id) {
    log.info("REQUEST: method = GET, path = /v1/certificates/{}", id);
    CertificateDTO certificate = certificateService.getById(id);
    log.info("RESPONSE: responseBody = {}", certificate);
    return ResponseEntity.ok(certificate);
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<CertificateDTO> getByName(@PathVariable String name) {
    log.info("REQUEST: method = GET, path = /v1/certificates/name/{}", name);
    CertificateDTO certificate = certificateService.getByName(name);
    log.info("RESPONSE: responseBody = {}", certificate);
    return ResponseEntity.ok(certificate);
  }

  @GetMapping
  public ResponseEntity<List<CertificateDTO>> getCertificateByNameOrDescription(
      @PageableDefault Pageable pageable,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String description) {
    log.info("REQUEST: method = GET, path = /v1/certificates, param = [name = {}, description = {}, pageable = {}]",
        name, description, pageable);
    List<CertificateDTO> listCertificate = certificateService.getByNameDescription(pageable, name, description);
    log.info("RESPONSE: responseBody = {}", listCertificate);
    return ResponseEntity.ok(listCertificate);
  }

  @GetMapping("/tags/{tagsNames}")
  public ResponseEntity<Set<CertificateDTO>> getCertificateByTagsName(@PathVariable List<String> tagsNames,
                                                                      @PageableDefault Pageable pageable) {
    log.info("REQUEST: method = GET, path = /v1/certificates/tags/{}, param = [pageable = {}]", tagsNames, pageable);
    Set<CertificateDTO> setCertificate = certificateService.getByTagsName(tagsNames, pageable);
    log.info("RESPONSE: responseBody = {}", setCertificate);
    return ResponseEntity.ok(setCertificate);
  }
}
