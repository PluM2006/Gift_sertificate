package ru.clevertec.ecl.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserCertificateKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "certificate_id")
    private Long certificateId;

}
