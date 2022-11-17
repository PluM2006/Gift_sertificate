package ru.clevertec.ecl.mapper;

import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;

@Mapper(imports = {LocalDateTime.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface OrderMapper {

  OrderDTO toOrderDto(Order order);

  @Mapping(target = "purchaseDate", expression = "java(LocalDateTime.now())")
  @Mapping(target = "user.orderList", ignore = true)
  Order toOrder(OrderDTO orderDTO);
}
