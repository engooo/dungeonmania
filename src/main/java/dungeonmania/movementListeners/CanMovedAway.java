package dungeonmania.movementListeners;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface CanMovedAway {
    void onMovedAway(GameMap map, Entity entity);
}
