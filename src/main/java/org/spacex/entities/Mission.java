package org.spacex.entities;

import java.util.ArrayList;
import java.util.List;

public class Mission {

  private final String name;

  private MissionStatus status;

  private final List<String> rocketIds = new ArrayList<>();

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

  public Mission setStatus(MissionStatus status) {
    this.status = status;
    return this;
  }

  public List<String> getRocketIds() {
    return rocketIds;
  }
}
