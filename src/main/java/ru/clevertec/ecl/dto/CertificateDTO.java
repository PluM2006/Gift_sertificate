package ru.clevertec.ecl.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Jacksonized
public class CertificateDTO extends AbstractDTO {

  @NotBlank
  private String name;
  private String description;

  @Positive
  private BigDecimal price;

  @Positive
  private Integer duration;
  private LocalDateTime createDate;
  private LocalDateTime lastUpdateDate;
  private List<TagDTO> tags;
}
