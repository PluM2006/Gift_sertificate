package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.OrderService;
import ru.clevertec.ecl.services.UserService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CertificateService certificateService;

    private final UserService userService;

    private final CertificateMapper certificateMapper;

    @Transactional
    @Override
    public Order addOrder(Order order) {
        CertificateDTO byName = certificateService.getByName(order.getCertificate().getName());
        User userByUserName = userService.getUserByUserName(order.getUser().getUsername());

        Order order1 = new Order(userByUserName, certificateMapper.toCertificate(byName));
        order1.setNumberOrder(UUID.randomUUID());
        order1.setPrice(byName.getPrice());
        return orderRepository.save(order1);
    }

    @Override
    public List<Order> getAllUserOrder(User user) {
        return orderRepository.findAllByUserOrderByNumberOrder(user);
    }

    @Override
    public Order getOrderUser(User user, UUID uuid) {
        return null;
    }

}
