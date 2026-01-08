package org.spacex.entities;

public class Rocket {

  private final String name;

  private final RocketStatus status;

  private String missionId;

  public Rocket(String name) {
    this.name = name;
    this.status = RocketStatus.ON_GROUND;
  }

  public String getName() {
    return name;
  }

  public RocketStatus getStatus() {
    return status;
  }

  public String getMissionId() {
    return missionId;
  }
}
