package dungeonmania.entities.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InvincibilityMovementStrategy implements MovementStrategy {
    @Override
    public Position getNewPosition(Entity entity, Game game, Position position) {
        GameMap map = game.getMap();
        Position nextPos;
        
        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), position);

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(position, Direction.RIGHT)
                : Position.translateBy(position, Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(position, Direction.UP)
                : Position.translateBy(position, Direction.DOWN);
        Position offset = position;

        if (plrDiff.getY() == 0 && map.canMoveTo(entity, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(entity, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(entity, moveX))
                offset = moveX;
            else if (map.canMoveTo(entity, moveY))
                offset = moveY;
            else
                offset = position;
        } else {
            if (map.canMoveTo(entity, moveY))
                offset = moveY;
            else if (map.canMoveTo(entity, moveX))
                offset = moveX;
            else
                offset = position;
        }
        nextPos = offset;
        return nextPos;
    }
}
