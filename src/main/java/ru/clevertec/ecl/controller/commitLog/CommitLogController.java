package ru.clevertec.ecl.controller.commitLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.services.commitLog.CommitLogService;

@Slf4j
@RestController
@RequestMapping("/commitlog")
@RequiredArgsConstructor
public class CommitLogController {

  private final CommitLogService commitLogService;

  @GetMapping(value = "/current")
  public long getCurrentSequence() {
    return commitLogService.getCurrentSequence();
  }

}
