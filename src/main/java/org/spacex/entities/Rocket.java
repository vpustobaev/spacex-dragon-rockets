package org.spacex.entities;

public class Rocket {

  private final String name;

  private RocketStatus status;

  private String missionId;

  public Rocket(String name) {
    this.name = name;
    this.status = RocketStatus.ON_GROUND;
  }

  public RocketStatus getStatus() {
    return status;
  }

  public void setStatus(RocketStatus status) {
    this.status = status;
  }

  public String getMissionId() {
    return missionId;
  }

  public void setMissionId(String missionId) {
    this.missionId = missionId;
  }
}
