package org.spacex;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.spacex.entities.Mission;
import org.spacex.entities.MissionStatus;
import org.spacex.entities.Rocket;
import org.spacex.entities.RocketStatus;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DragonRepositoryTest {

  private static DragonRepository dragonRepository;

  @BeforeEach
  void setUp() {
    dragonRepository = new DragonRepository();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void adds_new_Rocket() {

    String newRocketName = "New_Rocket";
    assertNull(dragonRepository.getRocket(newRocketName));

    dragonRepository.addNewRocket(newRocketName);

    Rocket rocket = dragonRepository.getRocket(newRocketName);

    assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
  }

  @Test
  void throws_exception_on_adding_existing_Rocket() {

    String newRocketName = "New_Rocket";
    dragonRepository.addNewRocket(newRocketName);

    Rocket rocket = dragonRepository.getRocket(newRocketName);
    assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());

    assertThrowsExactly(
        IllegalArgumentException.class, () -> dragonRepository.addNewRocket(newRocketName));
  }

  @Test
  void adds_new_Mission() {

    String newMissionName = "New_Mission";
    assertNull(dragonRepository.getMission(newMissionName));

    dragonRepository.addNewMission(newMissionName);

    Mission mission = dragonRepository.getMission(newMissionName);

    assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
  }

  @Test
  void throws_exception_on_adding_existing_Mission() {

    String newMissionName = "New_Mission";
    dragonRepository.addNewMission(newMissionName);

    Mission mission = dragonRepository.getMission(newMissionName);
    assertEquals(MissionStatus.SCHEDULED, mission.getStatus());

    assertThrowsExactly(
        IllegalArgumentException.class, () -> dragonRepository.addNewMission(newMissionName));
  }

  @Test
  void assigns_Rocket_To_Mission() {

    String rocketName = "Rocket";
    dragonRepository.addNewRocket(rocketName);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignRocketToMission(rocketName, missionName);

    Mission mission = dragonRepository.getMission(missionName);
    assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    assertTrue(mission.getRocketIds().contains(rocketName));

    Rocket rocket = dragonRepository.getRocket(rocketName);
    assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
  }

  @Test
  void grounding_last_Rocket_sets_MissionStatus_to_scheduled() {

    String rocketName1 = "Rocket1";
    dragonRepository.addNewRocket(rocketName1);

    String rocketName2 = "Rocket2";
    dragonRepository.addNewRocket(rocketName2);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignRocketToMission(rocketName1, missionName);
    dragonRepository.assignRocketToMission(rocketName2, missionName);

    dragonRepository.changeRocketStatus(rocketName1, RocketStatus.ON_GROUND);

    Mission mission = dragonRepository.getMission(missionName);
    assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());

    dragonRepository.changeRocketStatus(rocketName2, RocketStatus.ON_GROUND);
    mission = dragonRepository.getMission(missionName);
    assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
  }

  @Test
  void repairing_Rocket_sets_its_corresponding_MissionStatus_to_pending() {

    String rocketName = "Rocket";
    dragonRepository.addNewRocket(rocketName);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignRocketToMission(rocketName, missionName);

    dragonRepository.changeRocketStatus(rocketName, RocketStatus.IN_REPAIR);

    Mission mission = dragonRepository.getMission(missionName);
    assertEquals(MissionStatus.PENDING, mission.getStatus());
  }

  @Test
  void throws_Exception_on_changing_MissionStatus_to_In_Scheduled_when_rocket_is_assigned() {

    String rocketName = "Rocket";
    dragonRepository.addNewRocket(rocketName);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignRocketToMission(rocketName, missionName);

    IllegalArgumentException exception =
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> dragonRepository.changeMissionStatus(missionName, MissionStatus.SCHEDULED));

    assertEquals("Cannot set to 'Scheduled' while rockets are assigned.", exception.getMessage());
  }

  @Test
  void
      throws_Exception_on_changing_MissionStatus_to_In_Progress_when_minimum_one_assigned_Rocket_is_In_Repair() {

    String rocketName1 = "Rocket1";
    dragonRepository.addNewRocket(rocketName1);

    String rocketName2 = "Rocket2";
    dragonRepository.addNewRocket(rocketName2);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignRocketToMission(rocketName1, missionName);
    dragonRepository.assignRocketToMission(rocketName2, missionName);

    dragonRepository.changeRocketStatus(rocketName1, RocketStatus.IN_REPAIR);

    IllegalArgumentException exception =
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> dragonRepository.changeMissionStatus(missionName, MissionStatus.IN_PROGRESS));

    assertEquals(
        "Cannot set to 'In Progress' while one or more rockets are 'In repair'.",
        exception.getMessage());
  }

  @Test
  void throws_Exception_on_changing_MissionStatus_to_Pending_when_no_Rocket_is_In_Repair() {

    String rocketName1 = "Rocket1";
    dragonRepository.addNewRocket(rocketName1);

    String rocketName2 = "Rocket2";
    dragonRepository.addNewRocket(rocketName2);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignRocketToMission(rocketName1, missionName);
    dragonRepository.assignRocketToMission(rocketName2, missionName);

    IllegalArgumentException exception =
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> dragonRepository.changeMissionStatus(missionName, MissionStatus.PENDING));

    assertEquals(
        "Cannot set to 'Pending' while no rockets are 'In repair'.", exception.getMessage());
  }

  @Test
  void assigns_Rockets_To_Mission() {}

  @Test
  void gets_Missions_summary() {}
}
