package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrdersCertificatesDTO {

    private Long id;
    private BigDecimal price;
    private CertificateDTO certificate;

}
