package ru.clevertec.ecl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@EqualsAndHashCode(of = "name")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_certificate")
@DynamicUpdate
@NamedEntityGraphs(value = {
    @NamedEntityGraph(name = "certificateTag", attributeNodes = {
        @NamedAttributeNode("tags")
    })
})
public class Certificate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer duration;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime lastUpdateDate;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "certificate_tag",
      joinColumns = @JoinColumn(name = "certificate_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  @ToString.Exclude
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();
}
