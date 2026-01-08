package org.spacex.entities;

public enum RocketStatus {
  ON_GROUND("On ground"),
  IN_SPACE("In space"),
  IN_REPAIR("In repair");

  private final String name;

  RocketStatus(String name) {
    this.name = name;
  }

  public String getStatusName() {
    return name;
  }
}
