package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TagDTO {

    private Long id;
    private String name;
    @JsonBackReference
    private Set<CertificateDTO> certificates;
}
