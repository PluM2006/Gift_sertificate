package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class CertificateDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Set<TagDTO> tags;

}
