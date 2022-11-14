package ru.clevertec.ecl.entity.commitLog;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.commitlog.Operation;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"json", "entityName"})
public class CommitLog {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commitLog_id")
  @SequenceGenerator(name = "commitLog_id",
      allocationSize = 1)
  private Long id;
  private Operation operation;
  private String json;
  private String entityName;
  private Long entityId;
  private LocalDateTime dateTimeOperation;
}
