package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.OrderCertificate;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.OrderService;
import ru.clevertec.ecl.services.UserService;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CertificateService certificateService;

    private final CertificateMapper certificateMapper;


    @Override
    public Order addOrder(Order order) {
        Certificate certificate = certificateMapper.toCertificate(certificateService.getById(1L));



        OrderCertificate orderCertificate = new OrderCertificate();
        orderCertificate.setCertificate(certificate);
        orderCertificate.setPriceCertificate(certificate.getPrice());
        order.setCertificates(Arrays.asList(orderCertificate));


        return orderRepository.save(order);

    }

    @Override
    public Order getOrderByUsername(String username) {
        return null;
    }
}
