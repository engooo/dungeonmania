package dungeonmania.entities.movementListeners;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface ActionOnOverlap {
  void onOverlap(GameMap map, Entity entity);
}
