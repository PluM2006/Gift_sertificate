package ru.clevertec.ecl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@Builder
@EqualsAndHashCode(of =  "username")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String secondName;
    private String username;


}
