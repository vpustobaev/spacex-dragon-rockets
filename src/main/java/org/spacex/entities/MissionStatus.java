package org.spacex.entities;

public enum MissionStatus {

  SCHEDULED("Scheduled"),
  PENDING("Pending"),
  IN_PROGRESS("In Progress"),
  ENDED("Ended");

  private final String name;

  MissionStatus(String name) {
    this.name = name;
  }

  public String getStatusName() {
    return name;
  }
}
