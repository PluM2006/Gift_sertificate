package ru.clevertec.ecl.services.commitLog;

import java.util.List;
import ru.clevertec.ecl.recovery.Operation;
import ru.clevertec.ecl.dto.AbstractDTO;
import ru.clevertec.ecl.entity.commitLog.CommitLog;

public interface CommitLogService {

  CommitLog write(CommitLog commitLog);

  CommitLog buildCommitLog(Operation operation, AbstractDTO abstractDto, String table);

  long getCurrentSequence();

  List<CommitLog> getRecoveryData(int limit);

}

