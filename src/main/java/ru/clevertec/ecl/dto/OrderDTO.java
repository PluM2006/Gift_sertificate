package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.ecl.entity.OrderCertificate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDTO {

    private Long id;
    private UUID numberOrder;
    private LocalDateTime createDate;
    private UserDTO userDTO;
    private List<OrderCertificate> orderCertificates;
}
