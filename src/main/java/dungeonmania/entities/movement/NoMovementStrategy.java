package dungeonmania.entities.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class NoMovementStrategy implements MovementStrategy {
	@Override
	public Position getNewPosition(Entity entity, Game game, Position position) {
		return position;
	}

}
