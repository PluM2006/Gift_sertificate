package ru.clevertec.ecl.data;

import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CertificateFactory {

    public static Certificate certificate() {
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

    public static CertificateDTO certificateDTO() {
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
    public static Certificate certificateForExample() {
        return Certificate.builder()
                .name("Certificate 1")
                .description("The best certificate")
                .build();
    }
}
