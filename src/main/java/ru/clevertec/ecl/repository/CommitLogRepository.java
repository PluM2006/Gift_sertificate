package ru.clevertec.ecl.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.commitLog.CommitLog;

public interface CommitLogRepository extends JpaRepository<CommitLog, Long> {

  @Query(value = "SELECT last_value FROM commit_log_id", nativeQuery = true)
  long getCurrentSequence();

  @Query(nativeQuery = true,
      value = "SELECT c.id, c.date_time_operation, c.entity_id, c.json, c.operation, c.entity_name  "
          + "FROM  commit_log c ORDER BY c.id DESC limit :limits")
  List<CommitLog> findRecoveryData(int limits);

}
