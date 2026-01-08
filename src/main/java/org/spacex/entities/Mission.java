package org.spacex.entities;

import java.util.List;

public class Mission {

  private final String name;

  private MissionStatus status;

  private List<String> rocketIds;

  public Mission(String name) {
    this.name = name;
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
