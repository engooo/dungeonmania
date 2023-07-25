package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemyGoal implements Goal {
  private int target;

  public EnemyGoal(int target) {
    this.target = target;
  }

  public boolean achieved(Game game) {
    return game.getSlainEnemyCount() >= target && game.getMap().getEntities(ZombieToastSpawner.class).size() == 0;
  }

  public String toString(Game game) {
    if (achieved(game))
      return "";
    else
      return ":enemies";
  }
}
