package ru.clevertec.ecl.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;

public class CertificateTestData {

  public static Certificate buildCertificateOne() {
    return Certificate.builder()
        .id(1L)
        .name("Certificate 1")
        .description("The best certificate")
        .createDate(LocalDateTime.now())
        .lastUpdateDate(LocalDateTime.now())
        .price(BigDecimal.valueOf(20))
        .tags(new ArrayList<>())
        .build();
  }

  public static Certificate buildCertificateTwo() {
    return Certificate.builder()
        .id(2L)
        .name("Certificate 2")
        .description("The super best certificate")
        .createDate(LocalDateTime.now())
        .lastUpdateDate(LocalDateTime.now())
        .price(BigDecimal.valueOf(10))
        .tags(new ArrayList<>())
        .build();
  }

  public static CertificateDTO buildCertificateDTO() {
    return CertificateDTO.builder()
        .id(1L)
        .name("Certificate 1")
        .description("The best certificate")
        .createDate(LocalDateTime.now())
        .lastUpdateDate(LocalDateTime.now())
        .price(BigDecimal.valueOf(20))
        .tags(new ArrayList<>())
        .build();
  }

  public static Certificate buildCertificateForExample() {
    return Certificate.builder()
        .name("Certificate 1")
        .description("The best certificate")
        .build();
  }

  public static List<Certificate> buildCertificates() {
    return Arrays.asList(buildCertificateOne(), buildCertificateTwo());
  }
}
