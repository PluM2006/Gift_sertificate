package ru.clevertec.ecl.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

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
