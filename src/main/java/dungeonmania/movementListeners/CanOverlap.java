package dungeonmania.movementListeners;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface CanOverlap {
    void onOverlap(GameMap map, Entity entity);
}
