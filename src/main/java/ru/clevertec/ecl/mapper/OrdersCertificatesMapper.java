package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrdersCertificatesMapper {

    OrdersCertificates toOrdersCertificates(OrdersCertificatesDTO ordersCertificatesDTO);

    OrdersCertificatesDTO toOrdersCertificatesDTO(OrdersCertificates ordersCertificates);
}
