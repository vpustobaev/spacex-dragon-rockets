package org.spacex.entities;

import java.util.List;

public class Mission {

  private final String name;

  private final MissionStatus status;

  private List<String> rocketIds;

  public Mission(String name) {
    this.name = name;
    this.status = MissionStatus.SCHEDULED;
  }

  public String getName() {
    return name;
  }

  public MissionStatus getStatus() {
    return status;
  }

  public List<String> getRocketIds() {
    return rocketIds;
  }
}
