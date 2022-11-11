package ru.clevertec.ecl.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Jacksonized
public class OrderDTO extends AbstractDto{

  private BigDecimal price;
  private LocalDateTime purchaseDate;

  @NotNull
  private UserDTO user;

  @NotNull
  private CertificateDTO certificate;
}
