package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SceptreTest {
  @Test
  @DisplayName("Testing sceptre recipes")
  public void sceptreRecipes() {
    DungeonManiaController dmc = new DungeonManiaController();
    DungeonResponse res = dmc.newGame("d_SceptreTest_sceptreRecipes", "c_SceptreTest_sceptreRecipes");

    // Case1: Pick up 1 wood, 1 treasure, 1 sun_stone
    res = dmc.tick(Direction.RIGHT);
    res = dmc.tick(Direction.RIGHT);
    res = dmc.tick(Direction.RIGHT);
    assertEquals(1, TestUtils.getInventory(res, "wood").size());
    assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    // Build Sceptre
    assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
    res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    // Case2: Pick up 1 wood, 1 key, 1 sun_stone
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.LEFT);
    res = dmc.tick(Direction.LEFT);
    assertEquals(1, TestUtils.getInventory(res, "wood").size());
    assertEquals(1, TestUtils.getInventory(res, "key").size());
    assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    // Build Sceptre
    assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
    res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    assertEquals(2, TestUtils.getInventory(res, "sceptre").size());

    // Case3: Pick up 2 arrows, 1 treasure, 1 sun_stone
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.RIGHT);
    res = dmc.tick(Direction.RIGHT);
    res = dmc.tick(Direction.RIGHT);
    assertEquals(2, TestUtils.getInventory(res, "arrow").size());
    assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    // Build Sceptre
    assertEquals(2, TestUtils.getInventory(res, "sceptre").size());
    res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    assertEquals(3, TestUtils.getInventory(res, "sceptre").size());

    // Case4: Pick up 2 arrows, 1 key, 1 sun_stone
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.LEFT);
    res = dmc.tick(Direction.LEFT);
    res = dmc.tick(Direction.LEFT);
    assertEquals(2, TestUtils.getInventory(res, "arrow").size());
    assertEquals(1, TestUtils.getInventory(res, "key").size());
    assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    // Build Sceptre
    assertEquals(3, TestUtils.getInventory(res, "sceptre").size());
    res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    assertEquals(4, TestUtils.getInventory(res, "sceptre").size());

    // Case5: Pick up 2 arrows, 2 sun_stones
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.RIGHT);
    res = dmc.tick(Direction.RIGHT);
    res = dmc.tick(Direction.RIGHT);
    assertEquals(2, TestUtils.getInventory(res, "arrow").size());
    assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

    // Build Sceptre
    assertEquals(4, TestUtils.getInventory(res, "sceptre").size());
    res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    assertEquals(5, TestUtils.getInventory(res, "sceptre").size());
    // Check sun_stone remains
    assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    // Case6: Pick up 1 wood, 1 more sun_stone
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.LEFT); // sun_stone at this tile removed from dungeon
    res = dmc.tick(Direction.LEFT);
    assertEquals(1, TestUtils.getInventory(res, "wood").size());
    assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

    // Build Sceptre
    assertEquals(5, TestUtils.getInventory(res, "sceptre").size());
    res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    assertEquals(6, TestUtils.getInventory(res, "sceptre").size());
    // Check sun_stone remains
    assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    // Case7: uncraftable: Pick up 1 sun_stone, 1 wood (test they uncraftable)
    res = dmc.tick(Direction.DOWN); // sun_stone at this tile removed from dungeon
    res = dmc.tick(Direction.RIGHT);
    assertEquals(1, TestUtils.getInventory(res, "wood").size());
    assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    // Try to build Sceptre
    assertEquals(6, TestUtils.getInventory(res, "sceptre").size());
    assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));
  }

  @Test
  @DisplayName("Testing mind control")
  public void mindControl() {
    DungeonManiaController dmc = new DungeonManiaController();
    DungeonResponse res = dmc.newGame("d_SceptreTest_mindControl", "c_SceptreTest_mindControl");
    String mercenaryId = TestUtils.getEntities(res, "mercenary").get(0).getId();

    // Build sceptre
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.DOWN);
    res = assertDoesNotThrow(() -> dmc.build("sceptre"));

    // Mind control mercenary
    res = assertDoesNotThrow(() -> dmc.interact(mercenaryId));

    // walk into mercenary, a battle does not occur
    res = dmc.tick(Direction.DOWN);
    res = dmc.tick(Direction.DOWN);
    assertEquals(0, res.getBattles().size());

  }
}
