package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.inventory.InventoryItem;

/**
 * Item has buff in battles
 */
public interface BattleItem {
  public BattleStatistics applyBuff(BattleStatistics origin);

  public int getDurability();

  public void setDurability(int durability);

  default void use(Game game) {
    setDurability(getDurability() - 1);
    if (getDurability() <= 0) {
      game.getPlayer().remove((InventoryItem) this);
    }
  }
}
