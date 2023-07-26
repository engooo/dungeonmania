package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.movementListeners.ActionOnOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class CollectableEntity extends Entity implements InventoryItem, ActionOnOverlap {
  public CollectableEntity(Position position) {
    super(position);
  }

  @Override
  public void onOverlap(GameMap map, Entity entity) {
    if (entity instanceof Player) {
      if (!((Player) entity).pickUp(this))
        return;
      map.destroyEntity(this);
    }
  }

  @Override
  public boolean canMoveOnto(GameMap map, Entity entity) {
    return true;
  }
}
