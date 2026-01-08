package org.spacex;

import org.junit.jupiter.api.*;
import org.spacex.entities.Rocket;
import org.spacex.entities.RocketStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DragonRepositoryTest {

  private static DragonRepository dragonRepository;

  @BeforeAll
  static void setUp() {
    dragonRepository = new DragonRepository();
  }

  @AfterAll
  static void tearDown() {}

  @Test
  void adds_new_Rocket() {

    String newRocket = "New_Rocket";
    assertNull(dragonRepository.getRocket(newRocket));

    dragonRepository.addNewRocket(newRocket);

    Rocket rocket = dragonRepository.getRocket(newRocket);

    assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
  }

  @Test
  void assigns_Rocket_To_Mission() {}

  @Test
  void assigns_Rockets_To_Mission() {}

  @Test
  void adds_new_Mission() {}

  @Test
  void changes_Mission_status() {}

  @Test
  void gets_Missions_summary() {}
}
