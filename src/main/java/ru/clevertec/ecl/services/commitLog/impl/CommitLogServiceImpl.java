package ru.clevertec.ecl.services.commitLog.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.AbstractDto;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.entity.commitLog.Operation;
import ru.clevertec.ecl.repository.CommitLogRepository;
import ru.clevertec.ecl.services.commitLog.CommitLogService;

@Service
@RequiredArgsConstructor
public class CommitLogServiceImpl implements CommitLogService{

  private final CommitLogRepository commitLogRepository;
  private final ObjectMapper mapper;


  @Override
  public CommitLog write(CommitLog commitLog) {
    return commitLogRepository.save(commitLog);
  }

  @Override
  @SneakyThrows
  public CommitLog buildCommitLog(Operation operation, AbstractDto abstractDto, String table) {
    return CommitLog.builder()
        .operation(operation)
        .entityId(abstractDto.getId())
        .entityName(table)
        .dateTimeOperation(LocalDateTime.now())
        .json(mapper.writeValueAsString(abstractDto))
        .build();
  }

  @Override
  public long getCurrentSequence() {
    return commitLogRepository.getCurrentSequence();
  }

}
