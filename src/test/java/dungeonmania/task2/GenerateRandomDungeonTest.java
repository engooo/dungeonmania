package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;

public class GenerateRandomDungeonTest {
  private boolean wallAt(DungeonResponse res, int x, int y) {
    Position pos = new Position(x, y);
    return TestUtils.getEntitiesStream(res, "wall").anyMatch(it -> it.getPosition().equals(pos));
  }

  @Test
  @DisplayName("Test random dungeon generation")
  public void playerAndExitAndBorder() {
    int xStart = 1;
    int yStart = 1;
    int xEnd = 9;
    int yEnd = 9;
    DungeonManiaController dmc = new DungeonManiaController();
    DungeonResponse res = dmc.generateDungeon(10, 10, 10, 10, "c_basicGoalstToast_exit");

    // Check player has spawned
    Position playerPos = new Position(xStart, yStart);
    assertEquals(playerPos, TestUtils.getEntities(res, "player").get(0).getPosition());

    // Exit exists
    Position existPos = new Position(xEnd, yEnd);
    assertEquals(existPos, TestUtils.getEntities(res, "exit").get(0).getPosition());

    // Test Borders of the dungeon
    for (int x = 0; x < 11; x++) {
      // Check the top border
      assertTrue(wallAt(res, x, yStart));
      // Check the bottom border
      assertTrue(wallAt(res, x, yEnd));
    }

    for (int y = 0; y < 11; y++) {
      // Check the left border
      assertTrue(wallAt(res, xStart, y));
      // Check the right border
      assertTrue(wallAt(res, xEnd, y));
    }
  }

}
