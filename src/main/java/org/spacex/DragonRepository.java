package org.spacex;

import java.util.HashMap;
import java.util.Map;
import org.spacex.entities.Mission;
import org.spacex.entities.Rocket;

public class DragonRepository {

  private final Map<String, Rocket> rockets = new HashMap<>();
  private final Map<String, Mission> missions = new HashMap<>();

  public Rocket getRocket(String name) {

    return rockets.get(name);
  }

  public Mission getMission(String name) {

    return missions.get(name);
  }

  public void addNewRocket(String name) {

    if (rockets.containsKey(name)) {
      throw new IllegalArgumentException("Rocket '" + name + "' already exists.");
    }
    rockets.put(name, new Rocket(name));
  }

  public void addNewMission(String name) {

    if (missions.containsKey(name)) {
      throw new IllegalArgumentException("Mission '" + name + "' already exists.");
    }
    missions.put(name, new Mission(name));
  }

  public void assignRocketToMission(String name) {}

  public void assignRocketToMission() {}

  public void assignsRocketsToMission() {}

  public void changeMissionStatus() {}

  public void getMissionsSummary() {}

  public Map<String, Rocket> getRockets() {
    return rockets;
  }

  public Map<String, Mission> getMissions() {
    return missions;
  }
}
