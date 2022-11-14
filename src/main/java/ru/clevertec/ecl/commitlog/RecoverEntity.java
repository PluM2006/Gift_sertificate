package ru.clevertec.ecl.commitlog;

import java.util.List;
import ru.clevertec.ecl.dto.AbstractDTO;
import ru.clevertec.ecl.entity.commitLog.CommitLog;

public interface RecoverEntity {

  void recoveryEntity(List<CommitLog> commitLog, Class<? extends AbstractDTO> c);

}
