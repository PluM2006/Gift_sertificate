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
  private OrderMapper orderMapper;
  @Mock
  private UserMapper userMapper;
  private Order order;
  private OrderDTO orderDTO;
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    pageable = Pageable.ofSize(1);
    order = OrderFactory.order();
    orderDTO = OrderFactory.orderDTO();
  }

  @Test
  void addOrder() {
    given(userService.getUserById(any())).willReturn(UserFactory.userDTO());
    given(certificateService.getById(any())).willReturn(CertificateFactory.certificateDTO());
    given(orderMapper.toOrderDto(order)).willReturn(orderDTO);
    given(orderMapper.toOrder(orderDTO)).willReturn(order);
    given(orderRepository.save(order)).willReturn(order);
    OrderDTO saveOrder = orderService.addOrder(orderDTO);
    assertThat(saveOrder).isNotNull();
  }

  @Test
  void getAllUserOrder() {
    List<Order> orderList = new ArrayList<>();
    orderList.add(order);
    given(orderRepository.findAllByUser(UserFactory.user(), pageable)).willReturn(orderList);
    given(userMapper.toUser(UserFactory.userDTO())).willReturn(UserFactory.user());
    List<OrderDTO> allUserOrder = orderService.getAllUserOrders(UserFactory.userDTO(), pageable);
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
}
