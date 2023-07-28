package dungeonmania.entities.movementListeners;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface ActionOnMoveAway {
  void onMovedAway(GameMap map, Entity entity);
}
