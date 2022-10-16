package ru.clevertec.ecl.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.CertificateFactory;
import ru.clevertec.ecl.data.OrderFactory;
import ru.clevertec.ecl.data.UserFactory;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.UserService;

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
  private CertificateMapper certificateMapper;

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
    order = OrderFactory.buildOrder();
    orderDTO = OrderFactory.buildOrderDTO();
  }

  @Test
  void addOrderSuccessTest() {
    given(userService.getUserById(any())).willReturn(UserFactory.buildUserDTO());
    given(certificateService.getById(any())).willReturn(CertificateFactory.buildCertificateDTO());
    given(certificateMapper.toCertificate(any())).willReturn(CertificateFactory.buildCertificateOne());
    given(userMapper.toUser(any())).willReturn(UserFactory.buildUserOne());
    given(orderMapper.toOrderDto(order)).willReturn(orderDTO);
    given(orderRepository.save(order)).willReturn(order);
    OrderDTO saveOrder = orderService.addOrder(orderDTO);
    assertThat(saveOrder).isNotNull();
  }

  @Test
  void getAllUserOrderSuccessTest() {
    List<Order> orderList = new ArrayList<>();
    orderList.add(order);
    given(orderRepository.findAllByUser(UserFactory.buildUserOne(), pageable)).willReturn(orderList);
    given(userMapper.toUser(UserFactory.buildUserDTO())).willReturn(UserFactory.buildUserOne());
    List<OrderDTO> allUserOrder = orderService.getAllUserOrders(UserFactory.buildUserDTO(), pageable);
    assertAll(() -> assertThat(allUserOrder).isNotNull(),
        () -> assertThat(allUserOrder.size()).isEqualTo(1));
  }

  @Test
  void getOrderByIdSuccessTest() {
    given(orderRepository.findById(1L)).willReturn(Optional.ofNullable(order));
    given(orderMapper.toOrderDto(order)).willReturn(orderDTO);
    OrderDTO orderById = orderService.getOrderById(1L);
    assertThat(orderById).isNotNull();
  }
}
