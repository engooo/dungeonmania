package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class BaseState extends PlayerState {
  public BaseState(Player player) {
    super(player, false, false);
  }

  public BattleStatistics applyBuff(BattleStatistics origin) {
    // No Buff
    return origin;
  }
}
