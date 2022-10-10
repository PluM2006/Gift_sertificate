package ru.clevertec.ecl.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.Certificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @Mock
    private CertificateService certificateService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserMapper userMapper;
    private Order order;
    private OrderDTO orderDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = Pageable.ofSize(1);
        order = getOrder();
        orderDTO = getOrderDTO();
    }

    @Test
    void addOrder() {
        given(userService.getUserByUserName(any())).willReturn(getUserDTO());
        given(certificateService.getById(any())).willReturn(getCertificateDTO());
        given(orderMapper.toOrderDto(order)).willReturn(orderDTO);
        given(orderMapper.toOrder(orderDTO)).willReturn(order);
        given(orderRepository.save(order)).willReturn(order);
        OrderDTO saveOrder = orderService.addOrder(orderDTO);
        assertThat(saveOrder).isNotNull();
    }

    @Test
    void getAllUserOrder() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(getOrder());
        given(orderRepository.findAllByUser(getUser(), pageable)).willReturn(orderList);
        given(userMapper.toUser(getUserDTO())).willReturn(getUser());
        List<OrderDTO> allUserOrder = orderService.getAllUserOrder(getUserDTO(), pageable);
        assertAll(() -> assertThat(allUserOrder).isNotNull(),
                () -> assertThat(allUserOrder.size()).isEqualTo(1));
    }

    @Test
    void getOrderById() {
        given(orderRepository.findById(1L)).willReturn(Optional.ofNullable(order));
        given(orderMapper.toOrderDto(order)).willReturn(orderDTO);
        OrderDTO orderById = orderService.getOrderById(1L);
        assertThat(orderById).isNotNull();
    }

    private OrderDTO getOrderDTO() {
        return OrderDTO.builder()
                .id(1L)
                .price(new BigDecimal(10))
                .certificate(getCertificateDTO())
                .user(getUserDTO())
                .build();
    }

    private Order getOrder() {
        return Order.builder()
                .id(1L)
                .price(new BigDecimal(10))
                .certificate(getCertificate())
                .user((getUser()))
                .build();
    }

    private CertificateDTO getCertificateDTO() {
        return CertificateDTO.builder()
                .id(1L)
                .price(new BigDecimal(10))
                .createDate(LocalDateTime.now())
                .description("description")
                .duration(10)
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }

    private Certificate getCertificate() {
        return Certificate.builder()
                .id(1L)
                .price(new BigDecimal(10))
                .createDate(LocalDateTime.now())
                .description("description")
                .duration(10)
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .secondName("secondName")
                .firstName("firstName")
                .username("username")
                .build();
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .secondName("secondName")
                .firstName("firstName")
                .username("username")
                .build();
    }

}
