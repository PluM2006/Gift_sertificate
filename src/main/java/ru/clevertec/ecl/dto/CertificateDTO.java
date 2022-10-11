package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CertificateDTO {

    private Long id;

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
