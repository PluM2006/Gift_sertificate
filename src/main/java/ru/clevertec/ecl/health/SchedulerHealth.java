package ru.clevertec.ecl.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.services.HealthCheckService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerHealth {

  private final HealthCheckService healthCheckService;

  @Scheduled(fixedRate = 5000)
  public void test() {
    healthCheckService.checkHealthClusterNodes();
  }

}
