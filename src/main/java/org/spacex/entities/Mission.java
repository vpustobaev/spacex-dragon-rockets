package org.spacex.entities;

import java.util.ArrayList;
import java.util.List;

public class Mission {

  private final String name;
  private final List<String> rocketIds = new ArrayList<>();
  private MissionStatus status;

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

  public void setStatus(MissionStatus status) {
    this.status = status;
  }

  public List<String> getRocketIds() {
    return rocketIds;
  }

  @Override
  public String toString() {
    return "Mission{"
        + "name='"
        + name
        + '\''
        + ", status="
        + status
        + ", rocketIds="
        + rocketIds
        + '}';
  }
}
