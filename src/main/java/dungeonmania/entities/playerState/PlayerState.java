package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;

public abstract class PlayerState {
  private Player player;
  private boolean isInvincible = false;
  private boolean isInvisible = false;

  PlayerState(Player player, boolean isInvincible, boolean isInvisible) {
    this.player = player;
    this.isInvincible = isInvincible;
    this.isInvisible = isInvisible;
  }

  public boolean isInvincible() {
    return isInvincible;
  };

  public boolean isInvisible() {
    return isInvisible;
  };

  public Player getPlayer() {
    return player;
  }

  public void transition(Potion inEffective) {
    if (inEffective instanceof InvincibilityPotion) {
      player.setState(player.getInvincibleState());
    } else if (inEffective instanceof InvisibilityPotion) {
      player.setState(player.getInvisibleState());
    } else { // (if inEffective == null || Unexpected State)
      player.setState(player.getBaseState());
    }
  }

  public abstract BattleStatistics applyBuff(BattleStatistics origin);
}
