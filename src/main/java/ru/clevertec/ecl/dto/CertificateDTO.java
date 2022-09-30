package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class CertificateDTO {

    private Long id;

    @NotBlank
    private String name;
    private String description;

    @Min(1)
    private BigDecimal price;

    @Min(1)
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<TagDTO> tags;
}
