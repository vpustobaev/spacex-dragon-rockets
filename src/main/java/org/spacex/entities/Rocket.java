package org.spacex.entities;

public class Rocket {

  private final String name;

  private RocketStatus status;

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

  public Rocket setStatus(RocketStatus status) {
    this.status = status;
    return this;
  }

  public String getMissionId() {
    return missionId;
  }

  public Rocket setMissionId(String missionId) {
    this.missionId = missionId;
    return this;
  }
}
