package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderDTO {

    private Long id;
    private BigDecimal price;
    private LocalDateTime purchaseDate;

    @NotNull
    private UserDTO user;

    @NotNull
    private CertificateDTO certificate;
}
