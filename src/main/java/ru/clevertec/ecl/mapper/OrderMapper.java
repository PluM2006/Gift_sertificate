package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entity.Order;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(imports = {UUID.class, LocalDateTime.class},
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface OrderMapper {

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "certificateDTO", source = "certificate")
    OrderDTO toOrderDto(Order order);

    @Mapping(target = "user", source = "userDTO")
    @Mapping(target = "certificate", source = "certificateDTO")
    @Mapping(target = "numberOrder", source = "numberOrder", defaultExpression = "java(UUID.randomUUID())")
    @Mapping(target = "createDate", source = "createDate", defaultExpression = "java(LocalDateTime.now())")
    Order toOrder(OrderDTO orderDTO);
}
