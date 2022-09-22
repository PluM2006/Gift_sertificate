package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class CertificateDTO {

    private Long id;
    @NotNull
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Set<TagDTO> tags;

}
