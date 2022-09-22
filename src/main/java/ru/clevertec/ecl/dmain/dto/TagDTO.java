package ru.clevertec.ecl.dmain.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class TagDTO {

    private Long id;
    @NotNull
    private String name;
    private Set<CertificateDTO> certificates;

}
