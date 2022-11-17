package ru.clevertec.ecl.data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;

public class OrderTestData {

  public static OrderDTO buildOrderDTO() {
    return OrderDTO.builder()
        .id(1L)
        .price(new BigDecimal(10))
        .certificate(CertificateTestData.buildCertificateDTO())
        .user(UserTestData.buildUserDTO())
        .build();
  }

  public static Order buildOrder() {
    return Order.builder()
        .id(1L)
        .price(new BigDecimal(10))
        .certificate(CertificateTestData.buildCertificateOne())
        .user(UserTestData.buildUserOne())
        .build();
  }

  public static List<Order> buildOrders(){
    return Arrays.asList(buildOrder(), buildOrder());
  }
}
