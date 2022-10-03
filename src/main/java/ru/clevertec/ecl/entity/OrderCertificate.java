package ru.clevertec.ecl.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders_certificates")
@Data
public class OrderCertificate {

    @EmbeddedId
    private CertificateOrderKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("certificateId")
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    @Column
    private BigDecimal priceCertificate;

}
