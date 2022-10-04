package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderDTO {

    private Long id;
    private UUID numberOrder;
    private BigDecimal price;
    private LocalDateTime createDate;
    private UserDTO userDTO;
    private CertificateDTO certificateDTO;
}
