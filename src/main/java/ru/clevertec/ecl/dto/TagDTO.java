package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(exclude="certificates")

public class TagDTO {

    private Long id;
    private String name;
    @ToString.Exclude
    private Set<CertificateDTO> certificates;
}
