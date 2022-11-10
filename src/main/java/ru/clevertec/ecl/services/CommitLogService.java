package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.AbstractDto;
import ru.clevertec.ecl.entity.commitLog.CommitLog;
import ru.clevertec.ecl.entity.commitLog.Operation;

public interface CommitLogService {

  CommitLog write(CommitLog commitLog);
  CommitLog buildCommitLog(Operation operation, AbstractDto abstractDto, String table);


}
