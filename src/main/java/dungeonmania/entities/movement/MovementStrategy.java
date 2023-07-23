package dungeonmania.entities.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public interface MovementStrategy {
    Position getNewPosition(Entity entity, Game game, Position position);
}
