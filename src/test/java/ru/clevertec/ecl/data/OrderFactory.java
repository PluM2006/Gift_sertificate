package ru.clevertec.ecl.data;

import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;

import java.math.BigDecimal;

public class OrderFactory{

        public static OrderDTO orderDTO() {
            return OrderDTO.builder()
                    .id(1L)
                    .price(new BigDecimal(10))
                    .certificate(CertificateFactory.certificateDTO())
                    .user(UserFactory.userDTO())
                    .build();
        }

        public static Order order() {
            return Order.builder()
                    .id(1L)
                    .price(new BigDecimal(10))
                    .certificate(CertificateFactory.certificate())
                    .user(UserFactory.user())
                    .build();
        }
}
