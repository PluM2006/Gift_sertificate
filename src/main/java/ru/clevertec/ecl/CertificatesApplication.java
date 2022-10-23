package ru.clevertec.ecl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class CertificatesApplication {

  public static void main(String[] args) {
    SpringApplication.run(CertificatesApplication.class, args);
  }
}
