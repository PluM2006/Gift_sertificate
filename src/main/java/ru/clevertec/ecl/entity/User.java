package ru.clevertec.ecl.entity;

import lombok.*;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Order> orderList = new ArrayList<>();

}
