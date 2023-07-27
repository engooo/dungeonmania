package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class MidnightArmourTest {
    @Test
    @DisplayName("Cannot build armour")
    public void testMidnightArmourBuffs() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        String config = "c_midnightArmourTest_MidnightArmourBuffs";
        DungeonResponse res = controller.newGame("d_midnightArmourTest_MidnightArmourBuffs.json", config);

        // mercenary attack: 5, health: 20, same as zombie
        // player attack: 5, health: 100
        // midnight attack: 2, defence: 2

        List<EntityResponse> entities = res.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "sunstone"));
        assertEquals(2, TestUtils.countEntityOfType(entities, "sword"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "mercenary"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "zombie_toast_spawner"));

        // move right, pickup sword
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "sword"));

        // move right, battle mercenary
        res = controller.tick(Direction.RIGHT);
        assertTrue(res.getBattles().size() == 1);

        List<BattleResponse> battles = res.getBattles();
        BattleResponse firstBattle = battles.get(0);
        RoundResponse firstRound = firstBattle.getRounds().get(0);

        // This is the attack without the armour
        double enemyAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("mercenary_attack", config));
        // double playerHealth = Double.parseDouble(TestUtils.getValueFromConfigFile("player_health", config));

        // compare playerBaseAttack and playerDamageTaken later
        double playerBaseAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("player_base_attack", config));
        double swordAttackBonus = Double.parseDouble(TestUtils.getValueFromConfigFile("sword_attack", config));
        double playerAttack = playerBaseAttack + swordAttackBonus;

        assertEquals((playerAttack) / 5, -firstRound.getDeltaEnemyHealth(), 0.001);
        assertEquals((enemyAttack / 10), -firstRound.getDeltaCharacterHealth(), 0.001);

        // move right, should be on (4,1)
        res = controller.tick(Direction.RIGHT);

        // move right, pick up sunstone
        res = controller.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sunstone").size());

        // move right, pick up sword
        res = controller.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // build midnight armour (5th tick)
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> controller.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // move right (6th tick) (spawn zombie)
        res = controller.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countEntityOfType(entities, "zombie"));

        // move right, battle zombie (7th tick)
        res = controller.tick(Direction.RIGHT);
        assertTrue(res.getBattles().size() == 2);

        BattleResponse secondBattle = battles.get(1);
        RoundResponse secondRound = secondBattle.getRounds().get(1);

        // This is the attack with the armour
        double midnightAttackBonus = Double.parseDouble(
            TestUtils.getValueFromConfigFile("midnight_armour_attack", config));
        assertEquals((playerAttack + midnightAttackBonus) / 5, -secondRound.getDeltaEnemyHealth(), 0.001);

        double midnightDefenceBonus = Double.parseDouble(
            TestUtils.getValueFromConfigFile("midnight_armour_defence", config));
        assertEquals((enemyAttack - midnightDefenceBonus / 10), -secondRound.getDeltaCharacterHealth(), 0.001);

        // More damage dealt with armour
        assertTrue(secondRound.getDeltaEnemyHealth() < firstRound.getDeltaEnemyHealth());

        // Less damage taken with armour

    }

    @Test
    @DisplayName("Cannot craft with zombies in dungeon")
    public void midnightArmourZombie() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_midnightArmourZombie",
        "c_midnightArmourTest_midnightArmourZombie");

        // pick up sword and sunstone
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sunstone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // atempt to build armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        // add exception handling? like throw exceptrion since cant build idk
        res = dmc.build("midnight_armour");
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }
}
