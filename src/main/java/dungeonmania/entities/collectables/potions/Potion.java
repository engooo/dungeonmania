package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.collectables.CollectableEntity;
import dungeonmania.util.Position;

public abstract class Potion extends CollectableEntity implements BattleItem {
  private int duration;

  public Potion(Position position, int duration) {
    super(position);
    this.duration = duration;
  }

  public int getDuration() {
    return duration;
  }

  @Override
  public BattleStatistics applyBuff(BattleStatistics origin) {
    return origin;
  }

  @Override
  public int getDurability() {
    return 1;
  }

  @Override
  public void setDurability(int durability) {
    return;
  }
}
