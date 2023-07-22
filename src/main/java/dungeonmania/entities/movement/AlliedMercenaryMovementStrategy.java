package dungeonmania.entities.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AlliedMercenaryMovementStrategy implements MovementStrategy {
	@Override
	public Position getNewPosition(Entity entity, Game game, Position position) {
		GameMap map = game.getMap();
		Player player = game.getPlayer();

		if (entity instanceof Mercenary) {
			boolean isAdjacentToPlayer = ((Mercenary) entity).isAdjacentToPlayer();
			return isAdjacentToPlayer ? player.getPreviousDistinctPosition()
					: map.dijkstraPathFind(position, player.getPosition(), entity);
		}
		return null;
	}
}
