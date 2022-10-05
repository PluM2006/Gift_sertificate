package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDTO {

    private Long id;
    private UUID numberOrder;
    private LocalDateTime createDate;
    private ReadUserDTO userDTO;
    private List<OrdersCertificatesDTO> ordersCertificates;
}
