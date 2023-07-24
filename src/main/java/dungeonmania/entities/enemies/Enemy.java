package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.movement.MovementStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.movementListeners.CanDestroy;
import dungeonmania.movementListeners.CanOverlap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable, CanDestroy, CanOverlap {
    private BattleStatistics battleStatistics;
    private MovementStrategy movementStrategy;


    public Enemy(Position position, double health, double attack, MovementStrategy movement) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        this.battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
        this.movementStrategy = movement;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    public abstract void move(Game game);

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    protected abstract void determineMovementStrategy(Game game);
}
