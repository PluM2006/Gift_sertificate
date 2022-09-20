package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(exclude = "tags")

public class CertificateDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    @ToString.Exclude
    private Set<TagDTO> tags;
}
