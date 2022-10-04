package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
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
//        orderDTO.getOrderCertificates().setCertificateDTO(getCertificate(orderDTO.getCertificateDTO()));
//        orderDTO.setUserDTO(userService.getUserByUserName(orderDTO.getUserDTO().getUsername()));
//        orderDTO.getCertificateDTO()
//                .forEach(certificateDTO -> certificateDTO.setPrice(certificateDTO.getPrice()));
        return orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrder(orderDTO)));
    }

    private List<CertificateDTO> getCertificate(List<CertificateDTO> certificateDTOS){
        return certificateDTOS.stream().map(certificateDTO -> certificateService.getByName(certificateDTO.getName()))
                .collect(Collectors.toList());


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
