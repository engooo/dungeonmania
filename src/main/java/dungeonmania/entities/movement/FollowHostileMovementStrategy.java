package dungeonmania.entities.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class FollowHostileMovementStrategy implements MovementStrategy {
    @Override
    public Position getNewPosition(Entity entity, Game game, Position position) {
			Player player = game.getPlayer();
			GameMap map = game.getMap();
      return map.dijkstraPathFind(position, player.getPosition(), entity);         
    }
}
