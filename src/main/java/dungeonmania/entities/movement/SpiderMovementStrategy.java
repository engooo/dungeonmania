package dungeonmania.entities.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class SpiderMovementStrategy implements MovementStrategy {
	@Override
	public Position getNewPosition(Entity entity, Game game, Position position) {
		// TODO: Implement this strategy.
		return position;
	}

}
