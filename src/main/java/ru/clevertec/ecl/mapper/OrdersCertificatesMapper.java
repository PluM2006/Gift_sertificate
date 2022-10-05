package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.OrdersCertificatesDTO;
import ru.clevertec.ecl.entity.OrdersCertificates;

@Mapper(componentModel = "spring")
public interface OrdersCertificatesMapper {

    OrdersCertificates toOrdersCertificates(OrdersCertificatesDTO ordersCertificatesDTO);

    OrdersCertificatesDTO toOrdersCertificatesDTO(OrdersCertificates ordersCertificates);
}
