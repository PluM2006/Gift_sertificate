package ru.clevertec.ecl.data;

import java.math.BigDecimal;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;

public class OrderFactory {

  public static OrderDTO buildOrderDTO() {
    return OrderDTO.builder()
        .id(1L)
        .price(new BigDecimal(10))
        .certificate(CertificateFactory.buildCertificateDTO())
        .user(UserFactory.buildUserDTO())
        .build();
  }

  public static Order buildOrder() {
    return Order.builder()
        .id(1L)
        .price(new BigDecimal(10))
        .certificate(CertificateFactory.buildCertificateOne())
        .user(UserFactory.buildUserOne())
        .build();
  }
}
