package org.spacex;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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

    IllegalArgumentException exception =
        assertThrowsExactly(
            IllegalArgumentException.class, () -> dragonRepository.addNewRocket(newRocketName));

    assertEquals("Rocket '" + newRocketName + "' already exists.", exception.getMessage());
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

    IllegalArgumentException exception =
        assertThrowsExactly(
            IllegalArgumentException.class, () -> dragonRepository.addNewMission(newMissionName));

    assertEquals("Mission '" + newMissionName + "' already exists.", exception.getMessage());
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
  void throws_Exception_on_assigning_already_assigned_Rocket() {

    String rocketName = "Rocket";
    dragonRepository.addNewRocket(rocketName);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignRocketToMission(rocketName, missionName);

    IllegalArgumentException exception =
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> dragonRepository.assignRocketToMission(rocketName, missionName));

    assertEquals("Rocket is already assigned to mission: " + missionName, exception.getMessage());
  }

  @Test
  void throws_Exception_on_assigning_Rocket_to_ended_Mission() {

    String rocketName = "Rocket";
    dragonRepository.addNewRocket(rocketName);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.getMission(missionName).setStatus(MissionStatus.ENDED);

    IllegalArgumentException exception =
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> dragonRepository.assignRocketToMission(rocketName, missionName));

    assertEquals("Cannot assign rockets to an Ended mission", exception.getMessage());
  }

  @Test
  void assigns_multiple_Rockets_To_Mission() {

    String rocketName1 = "Rocket1";
    dragonRepository.addNewRocket(rocketName1);

    String rocketName2 = "Rocket2";
    dragonRepository.addNewRocket(rocketName2);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignsRocketsToMission(List.of(rocketName1, rocketName2), missionName);

    Mission mission = dragonRepository.getMission(missionName);
    assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    assertEquals(2, mission.getRocketIds().size());
  }

  @Test
  void ending_Mission_preliminary_sets_unassigns_all_rockets() {

    String rocketName1 = "Rocket1";
    dragonRepository.addNewRocket(rocketName1);

    String rocketName2 = "Rocket2";
    dragonRepository.addNewRocket(rocketName2);

    String missionName = "Mission";
    dragonRepository.addNewMission(missionName);

    dragonRepository.assignsRocketsToMission(List.of(rocketName1, rocketName2), missionName);

    Mission mission = dragonRepository.getMission(missionName);
    assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    assertEquals(2, mission.getRocketIds().size());

    dragonRepository.changeMissionStatus(missionName, MissionStatus.ENDED);

    assertEquals(MissionStatus.ENDED, mission.getStatus());
    assertEquals(0, mission.getRocketIds().size());
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
  void
      missions_summary_is_sorted_by_number_of_rockets_descending_and_name_descending_if_number_of_rockets_same() {

    String missionName1 = "Mars";
    dragonRepository.addNewMission(missionName1);

    String missionName2 = "Luna1";
    dragonRepository.addNewMission(missionName2);

    String missionName3 = "Double Landing";
    dragonRepository.addNewMission(missionName3);
    dragonRepository.changeMissionStatus(missionName3, MissionStatus.ENDED);

    String missionName4 = "Transit";
    dragonRepository.addNewMission(missionName4);

    String missionName5 = "Luna2";
    dragonRepository.addNewMission(missionName5);

    String missionName6 = "Vertical Landing";
    dragonRepository.addNewMission(missionName6);
    dragonRepository.changeMissionStatus(missionName6, MissionStatus.ENDED);

    String rocketName1 = "Dragon 1";
    dragonRepository.addNewRocket(rocketName1);

    String rocketName2 = "Dragon 2";
    dragonRepository.addNewRocket(rocketName2);

    dragonRepository.assignRocketToMission(rocketName1, missionName2);
    dragonRepository.assignRocketToMission(rocketName2, missionName2);
    dragonRepository.changeRocketStatus(rocketName2, RocketStatus.IN_REPAIR);

    String rocketName3 = "Red Dragon";
    dragonRepository.addNewRocket(rocketName3);

    String rocketName4 = "Dragon XL";
    dragonRepository.addNewRocket(rocketName4);

    String rocketName5 = "Falcon Heavy";
    dragonRepository.addNewRocket(rocketName5);

    dragonRepository.assignRocketToMission(rocketName3, missionName4);
    dragonRepository.assignRocketToMission(rocketName4, missionName4);
    dragonRepository.assignRocketToMission(rocketName5, missionName4);

    String expectedSummary =
"""
• Transit - In Progress - Dragons: 3
 o Red Dragon - In space
 o Dragon XL - In space
 o Falcon Heavy - In space
• Luna1 - Pending - Dragons: 2
 o Dragon 1 - In space
 o Dragon 2 - In repair
• Vertical Landing - Ended - Dragons: 0
• Mars - Scheduled - Dragons: 0
• Luna2 - Scheduled - Dragons: 0
• Double Landing - Ended - Dragons: 0
""";

    Assertions.assertEquals(expectedSummary, dragonRepository.getMissionsSummary());
  }
}
