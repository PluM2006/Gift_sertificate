package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.OrderService;
import ru.clevertec.ecl.services.UserService;
import ru.clevertec.ecl.utils.Constants;

import java.util.List;

import static java.util.stream.Collectors.*;

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
        orderDTO.setUser(userService.getUserById(orderDTO.getUser().getId()));
        orderDTO.setPrice(orderDTO.getCertificate().getPrice());
        return orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrder(orderDTO)));
    }

    @Override
    public List<OrderDTO> getAllUserOrder(UserDTO userDTO, Pageable pageable) {
        return orderRepository.findAllByUser(userMapper.toUser(userDTO), pageable).stream()
                .map(orderMapper::toOrderDto)
                .collect(toList());
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        return orderMapper.toOrderDto(orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ORDER, Constants.FIELD_NAME_ID, id)));
    }
}
