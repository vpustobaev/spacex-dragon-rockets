package org.spacex;

import static java.util.stream.Collectors.toMap;

import java.util.*;
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
    } else if (missionToUpdate != null) {
      updateMissionStatusOnRockets(missionToUpdate);
    }
  }

  public void changeMissionStatus(String missionName, MissionStatus newStatus) {

    Mission mission = getMission(missionName);
    List<String> rockets = mission.getRocketIds();

    if (newStatus == MissionStatus.SCHEDULED && !rockets.isEmpty()) {
      throw new IllegalArgumentException("Cannot set to 'Scheduled' while rockets are assigned.");
    }

    if (newStatus == MissionStatus.IN_PROGRESS) {

      if (rockets.stream()
          .anyMatch(rocket -> getRocket(rocket).getStatus() == RocketStatus.IN_REPAIR)) {

        throw new IllegalArgumentException(
            "Cannot set to 'In Progress' while one or more rockets are 'In repair'.");
      }
    }

    if (newStatus == MissionStatus.PENDING) {

      if (rockets.stream()
          .noneMatch(rocket -> getRocket(rocket).getStatus() == RocketStatus.IN_REPAIR)) {

        throw new IllegalArgumentException(
            "Cannot set to 'Pending' while no rockets are 'In repair'.");
      }
    }

    if (newStatus == MissionStatus.ENDED) {

      new ArrayList<>(rockets).forEach(this::unassignRocketFromMission);
    }

    mission.setStatus(newStatus);
  }

  public void assignRocketToMission(String rocketName, String missionName) {

    Rocket rocket = rockets.get(rocketName);
    Mission mission = missions.get(missionName);

    if (rocket == null || mission == null) {
      throw new IllegalArgumentException("Rocket or Mission does not exist.");
    }

    if (rocket.getMissionId() != null) {
      throw new IllegalArgumentException(
          "Rocket is already assigned to mission: " + rocket.getMissionId());
    }

    if (mission.getStatus() == MissionStatus.ENDED) {
      throw new IllegalArgumentException("Cannot assign rockets to an Ended mission");
    }

    rocket.setMissionId(missionName);
    rocket.setStatus(RocketStatus.IN_SPACE);
    mission.getRocketIds().add(rocketName);
    updateMissionStatusOnRockets(missionName);
  }

  public void assignsRocketsToMission(List<String> rockets, String missionName) {

    for (String rocket : rockets) {
      assignRocketToMission(rocket, missionName);
    }
  }

  public String getMissionsSummary() {

    StringBuilder sb = new StringBuilder();

    sortMissionsByRocketsNumberAndMissionNamesReversed()
        .values()
        .forEach(
            mission -> {
              sb.append("• ").append(mission.getName());
              sb.append(" - ").append(mission.getStatus().getStatusName());
              sb.append(" - Dragons: ").append(mission.getRocketIds().size());
              sb.append("\n");

              mission
                  .getRocketIds()
                  .forEach(
                      r -> {
                        sb.append(" o ").append(r);
                        sb.append(" - ").append(getRocket(r).getStatus().getStatusName());
                        sb.append("\n");
                      });
            });

    return sb.toString();
  }

  private void unassignRocketFromMission(String rocketToUnassign) {

    Rocket rocket = getRocket(rocketToUnassign);
    String missionName = rocket.getMissionId();

    if (missionName == null) {
      return;
    }

    Mission mission = missions.get(missionName);
    List<String> assignedRockets = mission.getRocketIds();

    assignedRockets.remove(rocketToUnassign);
    rocket.setMissionId(null);

    if (assignedRockets.isEmpty()) {
      mission.setStatus(MissionStatus.SCHEDULED);
    } else {
      updateMissionStatusOnRockets(missionName);
    }
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

  private Map<String, Mission> sortMissionsByRocketsNumberAndMissionNamesReversed() {

    return missions.values().stream()
        .sorted(
            Comparator.comparingInt((Mission mission) -> mission.getRocketIds().size())
                .thenComparing(Mission::getName)
                .reversed())
        .collect(
            toMap(
                Mission::getName,
                mission -> mission,
                (existing, replacement) -> existing,
                LinkedHashMap::new));
  }
}
