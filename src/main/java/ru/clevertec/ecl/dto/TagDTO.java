package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Value
@Builder
public class TagDTO {

    Long id;

    @NotBlank
    String name;
    List<CertificateDTO> certificates = new ArrayList<>();
}
