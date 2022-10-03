package ru.clevertec.ecl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateOrderKey implements Serializable {

    @Column(name = "certificate_id")
    private Long certificateId;
    @Column(name = "order_id")
    private Long orderId;

}
