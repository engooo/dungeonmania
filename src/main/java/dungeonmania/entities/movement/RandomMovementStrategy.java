package dungeonmania.entities.movement;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomMovementStrategy implements MovementStrategy {
	@Override
	public Position getNewPosition(Entity entity, Game game, Position position) {
		Random randGen = new Random();
		Position nextPos;
		GameMap map = game.getMap();

		List<Position> pos = position.getCardinallyAdjacentPositions();
		pos = pos.stream().filter(p -> map.canMoveTo(entity, p)).collect(Collectors.toList());
		if (pos.size() == 0) {
			nextPos = position;
			map.moveTo(entity, nextPos);
		} else {
			nextPos = pos.get(randGen.nextInt(pos.size()));
			map.moveTo(entity, nextPos);
		}
		
		return nextPos;
	}

}
