package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(imports = {LocalDateTime.class},
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface OrderMapper {

    OrderDTO toOrderDto(Order order);

    @Mapping(target = "purchaseDate", source = "purchaseDate", defaultExpression = "java(LocalDateTime.now())")
    Order toOrder(OrderDTO orderDTO);
}
