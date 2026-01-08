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
  void assigns_Rocket_To_Mission() {}

  @Test
  void assigns_Rockets_To_Mission() {}

  @Test
  void changes_Mission_status() {}

  @Test
  void gets_Missions_summary() {}
}
