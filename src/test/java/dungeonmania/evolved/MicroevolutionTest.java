package dungeonmania.evolved;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class MicroevolutionTest {

// Basic test, 3 enemies slain and spawner destroyed

    @Test
    @Tag("10-7")
    @DisplayName("Testing destroying a zombie toast spawner")
    public void toastDestruction() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_toastDestruction", "c_zombieTest_toastDestruction");
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // cardinally adjacent: true, has sword: false
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move right
        res = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true, but invalid_id
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("random_invalid_id"));
        // cardinally adjacent: true, has sword: true
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.countType(res, "zombie_toast_spawner"));
    }

    @Test
    @Tag("8-7")
    // Set target for enemyGoal to be 1 and use bomb to destroy spawner and enemy
    @DisplayName("Test surrounding entities are removed when placing "
            + "a bomb next to an active switch with bomb radius set to 2")
    public void placeBombRadius2() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

        // Check Bomb exploded with radius 2
        //
        //                 Boulder/Switch        Wall            Wall
        //                Bomb                   Treasure
        //
        //                Treasure
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(0, TestUtils.getEntities(res, "treasure").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

}
