package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movement.SpiderMovementStrategy;
import dungeonmania.util.Position;

public class Spider extends Enemy {
    private List<Position> movementTrajectory;
    private int nextPositionElement;
    private boolean forward;

    public List<Position> getMovementTrajectory() {
        return movementTrajectory;
    }

    public int getNextPositionElement() {
        return nextPositionElement;
    }

    public static final int DEFAULT_SPAWN_RATE = 0;
    public static final double DEFAULT_ATTACK = 5;
    public static final double DEFAULT_HEALTH = 10;

    public Spider(Position position, double health, double attack) {
        super(position.asLayer(Entity.DOOR_LAYER + 1), health, attack, null);
        /**
         * Establish spider movement trajectory Spider moves as follows:
         * 8 1 2 10/12 1/9 2/8
         * 7 S 3 11 S 3/7
         * 6 5 4 B 5 4/6
         */
        movementTrajectory = position.getAdjacentPositions();
        nextPositionElement = 1;
        forward = true;
    };

    private void updateNextPosition() {
        if (forward) {
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        } else {
            nextPositionElement--;
            if (nextPositionElement == -1) {
                nextPositionElement = 7;
            }
        }
    }

    @Override
    public void move(Game game) {
        determineMovementStrategy(game);
        Position nextPos = getMovementStrategy().getNewPosition(this, game, getPosition());
        List<Entity> entities = game.getMap().getEntities(nextPos);

        // Additional Error Checking
        processEntitiesInPosition(entities);

        // Check the new position again, it may have changed as a side-effect of
        // processEntites
        nextPos = getMovementStrategy().getNewPosition(this, game, getPosition());
        entities = game.getMap().getEntities(nextPos);

        if (entities.stream().allMatch(e -> e.canMoveOnto(game.getMap(), this))) {
            game.getMap().moveTo(this, nextPos);
            updateNextPosition();
        }
    }

    private void processEntitiesInPosition(List<Entity> entities) {
        // If there is a (at least one) boulder in the position, flip the direction of
        // the spider and update its position.
        if (entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            updateNextPosition();
            updateNextPosition();
        }
    }

    @Override
    protected void determineMovementStrategy(Game game) {
        // Spider Movement Strategy
        this.setMovementStrategy(new SpiderMovementStrategy());
    }
}
