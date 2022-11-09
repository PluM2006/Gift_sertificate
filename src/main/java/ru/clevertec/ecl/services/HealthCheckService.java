package ru.clevertec.ecl.services;

import java.util.List;

public interface HealthCheckService {

  void checkHealthClusterNodes();
  boolean isWorking(Integer port);
  List<Integer> getWorkingClusterShards();

}
