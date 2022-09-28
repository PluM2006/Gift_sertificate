package ru.clevertec.ecl.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
//
//    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
//    @ToString.Exclude
//    @Builder.Default
//    private List<Certificate> certificates = new ArrayList<>();

}
