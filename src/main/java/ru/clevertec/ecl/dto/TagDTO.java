package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TagDTO {

    private Long id;
    private String name;
    private Set<CertificateDTO> certificates;

}
