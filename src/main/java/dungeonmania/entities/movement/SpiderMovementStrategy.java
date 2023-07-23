package dungeonmania.entities.movement;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;

public class SpiderMovementStrategy implements MovementStrategy {
  @Override
  public Position getNewPosition(Entity entity, Game game, Position position) {
    if (entity instanceof Spider) {
      Spider spider = ((Spider) entity);
      List<Position> movementTrajectory = spider.getMovementTrajectory();
      int nextPositionElement = spider.getNextPositionElement();
      return movementTrajectory.get(nextPositionElement);
    } else {
      // Two ideas here. Returning position means that an incorrect entity does not
      // move. Returning null will cause errors and is easier to debug but bad for
      // player experience.

      // return position;
      return null;
    }
  }

}
