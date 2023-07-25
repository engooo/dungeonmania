package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MicroevolutionTest {
  @Test
  @DisplayName("Test achieving an enemy goal with single zombie and no spawner")
  public void oneEnemy() {
    DungeonManiaController dmc;
    dmc = new DungeonManiaController();
    DungeonResponse res = dmc.newGame("d_taskTwoTest_oneEnemy", "c_taskTwoTest_oneEnemy");

    // move player to right to pickup sword
    res = dmc.tick(Direction.RIGHT);

    // assert goal not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    // move player to kill zombie
    res = dmc.tick(Direction.RIGHT);
    // move right twice as zombie position is not certain
    res = dmc.tick(Direction.RIGHT);

    // assert goal met
    assertEquals("", TestUtils.getGoals(res));
  }

  @Test
  @DisplayName("Test achieving an enemy goal with single zombie and spawner")
  public void oneEnemyAndSpawner() {
    DungeonManiaController dmc;
    dmc = new DungeonManiaController();
    DungeonResponse res = dmc.newGame("d_taskTwoTest_oneEnemyAndSpawner", "c_taskTwoTest_oneEnemyAndSpawner");

    String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

    // move player to right to pickup sword
    res = dmc.tick(Direction.RIGHT);

    // assert goal not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    // move player to kill zombie
    res = dmc.tick(Direction.RIGHT);

    // move right twice as zombie position is not certain
    res = dmc.tick(Direction.RIGHT);

    // assert goal still not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    // destroy spawner
    res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

    // assert goal met
    assertEquals("", TestUtils.getGoals(res));
  }

  @Test
  @DisplayName("Test multiple enemies destroy goal with multiple spawners")
  public void multipleEnemyAndSpawner() {
    DungeonManiaController dmc;
    dmc = new DungeonManiaController();
    DungeonResponse res = dmc.newGame("d_taskTwoTest_multipleEnemyAndSpawner", "c_taskTwoTest_multipleEnemyAndSpawner");

    String spawnerId1 = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();
    String spawnerId2 = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

    // goal is only two zombies, but kill three in this test for the edge case

    // move player to right to pickup sword
    res = dmc.tick(Direction.RIGHT);

    // move player to kill first zombie
    res = dmc.tick(Direction.RIGHT);

    // assert goal not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    // move player to kill second zombie
    res = dmc.tick(Direction.RIGHT);

    // move player to kill third zombie
    res = dmc.tick(Direction.RIGHT);
    // one more in case zombies moved
    res = dmc.tick(Direction.RIGHT);

    // destroy first spawner
    res = assertDoesNotThrow(() -> dmc.interact(spawnerId1));

    // assert goal still not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    // move right to second spawner and destroy it
    res = dmc.tick(Direction.RIGHT);
    res = assertDoesNotThrow(() -> dmc.interact(spawnerId2));

    assertEquals("", TestUtils.getGoals(res));
  }

  @Test
  @DisplayName("Test achieving enemy goal with bomb")
  public void bombDestroyEnemy() {
    DungeonManiaController dmc;
    dmc = new DungeonManiaController();
    DungeonResponse res = dmc.newGame("d_taskTwoTest_bombEnemy", "c_taskTwoTest_bombEnemy");

    // assert goal not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    // move player to right to push boulder onto switch and activate bomb
    res = dmc.tick(Direction.RIGHT);

    // assert goal met
    assertEquals("", TestUtils.getGoals(res));
  }

  @Test
  @DisplayName("Test achieving enemy goal within a complex goal")
  public void enemyComplexGoal() {
    DungeonManiaController dmc;
    dmc = new DungeonManiaController();
    DungeonResponse res = dmc.newGame("d_taskTwoTest_enemyComplexGoal", "c_taskTwoTest_enemyComplexGoal");

    String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

    // move player to right to pickup sword
    res = dmc.tick(Direction.RIGHT);

    // assert goal not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));
    assertTrue(TestUtils.getGoals(res).contains(":exit"));

    // move player to kill zombie
    res = dmc.tick(Direction.RIGHT);
    res = dmc.tick(Direction.RIGHT);

    // assert goal still not met
    assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    // destroy the spawner
    res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

    // assert goal partially met
    assertFalse(TestUtils.getGoals(res).contains(":enemies"));
    assertTrue(TestUtils.getGoals(res).contains(":exit"));

    // move left back to exit
    res = dmc.tick(Direction.LEFT);
    res = dmc.tick(Direction.LEFT);
    res = dmc.tick(Direction.LEFT);
    res = dmc.tick(Direction.LEFT);

    // assert goal met
    assertEquals("", TestUtils.getGoals(res));
  }
}
