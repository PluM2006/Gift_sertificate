package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.OrderService;
import ru.clevertec.ecl.services.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CertificateMapper certificateMapper;
    private final CertificateService certificateService;

    @Transactional
    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toOrder(orderDTO);
        order.getOrdersCertificates().forEach(oc ->
        {
            oc.setCertificate(certificateMapper
                    .toCertificate(certificateService.getByName(oc.getCertificate().getName())));
            oc.setPrice(oc.getCertificate().getPrice());
            oc.setOrder(order);
        });
        UserDTO userByUserName = userService.getUserByUserName(orderDTO.getUserDTO().getUsername());
        order.setUser(userMapper.toUser(userByUserName));
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDTO> getAllUserOrder(UserDTO userDTO) {
        return orderRepository.findAllByUserOrderByNumberOrder(userMapper.toUser(userDTO))
                .stream().map(orderMapper::toOrderDto).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderByNumberOrder(UUID uuid) {
        return orderMapper.toOrderDto(orderRepository.findByNumberOrder(uuid));
    }
}
