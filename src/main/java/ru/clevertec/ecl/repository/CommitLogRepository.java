package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.commitLog.CommitLog;

public interface CommitLogRepository extends JpaRepository<CommitLog, Long> {

  @Query(value = "SELECT last_value FROM commit_log_id", nativeQuery = true)
  long getCurrentSequence();

}
