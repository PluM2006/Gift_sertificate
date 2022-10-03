package ru.clevertec.ecl.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "orders")
public class Order {

    @EmbeddedId
    private UserCertificateKey id;

    private UUID numberOrder;

    @ManyToOne
    @MapsId("userId")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @MapsId("certificateId")
    @ToString.Exclude
    private Certificate certificate;

    private BigDecimal price;

    public Order(User user, Certificate certificate){
        this.id = new UserCertificateKey(user.getId(), certificate.getId());
        this.user = user;
        this.certificate = certificate;

    }
}
