package ru.clevertec.ecl.services.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.CertificateDTO;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.commitlog.Operation;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.commitLog.CommitLogService;
import ru.clevertec.ecl.services.OrderService;
import ru.clevertec.ecl.services.UserService;
import ru.clevertec.ecl.utils.Constants;
import ru.clevertec.ecl.utils.OffsetLimitPageable;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final UserService userService;
  private final UserMapper userMapper;
  private final CertificateService certificateService;
  private final CertificateMapper certificateMapper;
  private final CommitLogService commitLogService;

  @Transactional
  @Override
  public OrderDTO addOrder(OrderDTO orderDTO) {
    OrderDTO saveOrder = orderMapper.toOrderDto(orderRepository.save(toBuildOrder(orderDTO)));
    log.info("CommitLog: save Order");
    CommitLog commitLog = commitLogService.buildCommitLog(Operation.SAVE, saveOrder, Constants.ORDER);
    log.info("Result commitLog: {}", commitLogService.write(commitLog));
    return saveOrder;
  }

  @Override
  public List<OrderDTO> getAllUserOrders(UserDTO userDTO, Pageable pageable) {
    return orderRepository.findAllByUser(userMapper.toUser(userDTO), pageable).stream()
        .map(orderMapper::toOrderDto)
        .collect(toList());
  }

  @Override
  public List<OrderDTO> getAllUserOrdersOffset(UserDTO userDTO, int limit, int offset) {
    Pageable pageable = new OffsetLimitPageable(limit, offset);
    return orderRepository.findAllByUser(userMapper.toUser(userDTO), pageable).stream()
        .map(orderMapper::toOrderDto)
        .collect(toList());
  }

  @Override
  public OrderDTO getOrderById(Long id) {
    return orderMapper.toOrderDto(orderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.ORDER, Constants.FIELD_NAME_ID, id)));
  }

  @Transactional
  @Override
  public long setSequence(Long seq) {
    return orderRepository.setSequence(seq);
  }

  @Override
  public long getLastValueSequence() {
    return orderRepository.getLastValueSequence();
  }

  private Order toBuildOrder(OrderDTO orderDTO) {
    CertificateDTO certificateDTObyId = certificateService.getById(orderDTO.getCertificate().getId());
    return Order.builder()
        .certificate(certificateMapper.toCertificate(certificateDTObyId))
        .user(userMapper.toUser(userService.getUserById(orderDTO.getUser().getId())))
        .price(certificateDTObyId.getPrice())
        .purchaseDate(LocalDateTime.now())
        .build();
  }
}
