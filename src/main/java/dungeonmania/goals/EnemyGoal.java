package dungeonmania.goals;

import dungeonmania.Game;

public class EnemyGoal implements Goal {
  private int target;

  public EnemyGoal(int target) {
    this.target = target;
  }

  public boolean achieved(Game game) {
    return false;
  }

  public String toString(Game game) {
    if (achieved(game))
      return "";
    else
      return ":enemies";
  }
}
