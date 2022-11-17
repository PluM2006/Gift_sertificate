package ru.clevertec.ecl.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.CertificateTestData;
import ru.clevertec.ecl.data.OrderTestData;
import ru.clevertec.ecl.data.UserTestData;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.UserService;
import ru.clevertec.ecl.services.commitLog.CommitLogService;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @InjectMocks
  private OrderServiceImpl orderService;

  @Mock
  private CommitLogService commitLogService;

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
    order = OrderTestData.buildOrder();
    orderDTO = OrderTestData.buildOrderDTO();
  }

  @Test
  void addOrderSuccessTest() {
    given(userService.getUserById(any())).willReturn(UserTestData.buildUserDTO());
    given(certificateService.getById(any())).willReturn(CertificateTestData.buildCertificateDTO());
    given(certificateMapper.toCertificate(any())).willReturn(CertificateTestData.buildCertificateOne());
    given(userMapper.toUser(any())).willReturn(UserTestData.buildUserOne());
    given(orderMapper.toOrderDto(order)).willReturn(orderDTO);
    given(orderRepository.save(order)).willReturn(order);
    OrderDTO saveOrder = orderService.addOrder(orderDTO);
    assertThat(saveOrder).isNotNull();
  }

  @Test
  void getAllUserOrderSuccessTest() {
    List<Order> orderList = OrderTestData.buildOrders();
    given(orderRepository.findAllByUser(UserTestData.buildUserOne(), pageable)).willReturn(orderList);
    given(userMapper.toUser(UserTestData.buildUserDTO())).willReturn(UserTestData.buildUserOne());
    List<OrderDTO> allUserOrder = orderService.getAllUserOrders(UserTestData.buildUserDTO(), pageable);
    assertAll(() -> assertThat(allUserOrder).isNotNull(),
        () -> assertThat(allUserOrder.size()).isEqualTo(2));
  }

  @Test
  void getOrderByIdSuccessTest() {
    given(orderRepository.findById(1L)).willReturn(Optional.ofNullable(order));
    given(orderMapper.toOrderDto(order)).willReturn(orderDTO);
    OrderDTO orderById = orderService.getOrderById(1L);
    assertThat(orderById).isNotNull();
  }
}
