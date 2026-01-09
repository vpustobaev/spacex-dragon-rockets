package org.spacex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.spacex.entities.Mission;
import org.spacex.entities.MissionStatus;
import org.spacex.entities.Rocket;
import org.spacex.entities.RocketStatus;

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

  public void changeRocketStatus(String rocketName, RocketStatus newStatus) {

    Rocket rocket = getRocket(rocketName);

    String missionToUpdate = rocket.getMissionId();

    rocket.setStatus(newStatus);

    if (newStatus == RocketStatus.ON_GROUND) {
      unassignRocketFromMission(rocketName);
    }

    if (missionToUpdate != null) {
      updateMissionStatusOnRockets(missionToUpdate);
    }
  }

  public void changeMissionStatus(String missionName, MissionStatus newStatus) {

    Mission mission = getMission(missionName);

    if (newStatus == MissionStatus.SCHEDULED && !mission.getRocketIds().isEmpty()) {
      throw new IllegalArgumentException("Cannot set to 'Scheduled' while rockets are assigned.");
    }

    List<String> rocketIds = mission.getRocketIds();

    if (newStatus == MissionStatus.IN_PROGRESS) {

      if (rocketIds.stream()
          .anyMatch(rocket -> getRocket(rocket).getStatus() == RocketStatus.IN_REPAIR)) {

        throw new IllegalArgumentException(
            "Cannot set to 'In Progress' while one or more rockets are 'In repair'.");
      }
    }

    if (newStatus == MissionStatus.PENDING) {

      if (rocketIds.stream()
          .noneMatch(rocket -> getRocket(rocket).getStatus() == RocketStatus.IN_REPAIR)) {

        throw new IllegalArgumentException(
            "Cannot set to 'Pending' while no rockets are 'In repair'.");
      }
    }

    getMission(missionName).setStatus(newStatus);
  }

  private void unassignRocketFromMission(String rocketName) {

    for (Mission mission : missions.values()) {

      if (mission.getRocketIds().contains(rocketName)) {

        mission.getRocketIds().remove(rocketName);

        if (mission.getRocketIds().isEmpty()) {
          mission.setStatus(MissionStatus.SCHEDULED);
        } else {
          updateMissionStatusOnRockets(mission.getName());
        }

        break;
      }
    }
  }

  public void assignRocketToMission(String rocketName, String missionName) {

    Rocket rocket = rockets.get(rocketName);
    Mission mission = missions.get(missionName);

    if (rocket == null || mission == null) {
      throw new IllegalArgumentException("Rocket or Mission does not exist.");
    }

    if (rocket.getMissionId() != null) {
      throw new IllegalStateException(
          "Rocket is already assigned to mission: " + rocket.getMissionId());
    }

    if (mission.getStatus() == MissionStatus.ENDED) {
      throw new IllegalStateException("Cannot assign rockets to an Ended mission");
    }

    rocket.setMissionId(missionName);
    rocket.setStatus(RocketStatus.IN_SPACE);
    mission.getRocketIds().add(rocketName);
    updateMissionStatusOnRockets(missionName);
  }

  private void updateMissionStatusOnRockets(String missionName) {

    List<String> rockets = missions.get(missionName).getRocketIds();

    if (rockets.isEmpty()) {
      changeMissionStatus(missionName, MissionStatus.SCHEDULED);
      return;
    }

    boolean hasRocketInRepair = false;

    for (String rocket : rockets) {

      if (getRocket(rocket).getStatus() == RocketStatus.IN_REPAIR) {
        hasRocketInRepair = true;
        break;
      }
    }

    if (hasRocketInRepair) {
      changeMissionStatus(missionName, MissionStatus.PENDING);
    } else {
      changeMissionStatus(missionName, MissionStatus.IN_PROGRESS);
    }
  }

  public void assignsRocketsToMission() {}

  public void getMissionsSummary() {}

  public Map<String, Rocket> getRockets() {
    return rockets;
  }

  public Map<String, Mission> getMissions() {
    return missions;
  }
}
