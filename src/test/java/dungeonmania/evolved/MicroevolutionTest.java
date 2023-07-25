package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicGoalsTest {
	@Test
	@DisplayName("Test achieving an enemy goal with single zombie and no spawner")
	public void oneEnemy() {
		DungeonManiaController dmc;
		dmc = new DungeonManiaController();
		DungeonResponse res = dmc.newGame("d_task2Test_oneEnemy", "c_task2Test_oneEnemy");

	}

	@Test
	@DisplayName("Test achieving an enemy goal with single zombie and spawner")
	public void oneEnemyAndSpawner() {
		DungeonManiaController dmc;
		dmc = new DungeonManiaController();
		DungeonResponse res = dmc.newGame("d_task2Test_oneEnemyAndSpawner", "c_task2Test_oneEnemyAndSpawner");

	}

	@Test
	@DisplayName("Test multiple enemies destroy goal with multiple spawners")
	public void multipleEnemyAndSpawner() {
		DungeonManiaController dmc;
		dmc = new DungeonManiaController();
		DungeonResponse res = dmc.newGame("d_c_task2Test_multipleEnemyAndSpawner", "c_c_task2Test_multipleEnemyAndSpawner");

	}

	@Test
	@DisplayName("Test achieving enemy goal with bomb")
	public void bombDestroyEnemy() {
		DungeonManiaController dmc;
		dmc = new DungeonManiaController();
		DungeonResponse res = dmc.newGame("d_task2Test_bombDestroyEnemy", "c_task2Test_bombDestroyEnemy");

	}

	@Test
	@DisplayName("Test achieving enemy goal within a complex goal")
	public void enemyComplexGoal() {
		DungeonManiaController dmc;
		dmc = new DungeonManiaController();
		DungeonResponse res = dmc.newGame("d_task2Test_enemyComplexGoal", "c_task2Test_enemyComplexGoal");

	}
}
