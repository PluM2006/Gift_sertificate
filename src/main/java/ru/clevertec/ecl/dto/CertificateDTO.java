package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Value
@Builder
public class CertificateDTO {

    Long id;

    @NotBlank
    String name;
    String description;
    BigDecimal price;
    Integer duration;
    LocalDateTime createDate;
    LocalDateTime lastUpdateDate;
    List<TagDTO> tags= new ArrayList<>();
}
