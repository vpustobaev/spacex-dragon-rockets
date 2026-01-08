package org.spacex;

import org.junit.jupiter.api.*;

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
  void adds_new_Rocket() {}

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
