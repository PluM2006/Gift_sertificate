package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.exception.NotFoundException;
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
    private final CertificateService certificateService;

    @Transactional
    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        orderDTO.setCertificate(certificateService.getById(orderDTO.getCertificate().getId()));
        orderDTO.setUser(userService.getUserByUserName(orderDTO.getUser().getUsername()));
        orderDTO.setPrice(orderDTO.getCertificate().getPrice());
        return orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrder(orderDTO)));
    }

    @Override
    public List<OrderDTO> getAllUserOrder(UserDTO userDTO) {
        return orderRepository.findAllByUser(userMapper.toUser(userDTO))
                .stream().map(orderMapper::toOrderDto).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        return orderMapper.toOrderDto(orderRepository.findById(id).orElseThrow(()-> new NotFoundException("Order", "id", id)));
    }
}
