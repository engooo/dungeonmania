package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.movement.InvincibilityMovementStrategy;
import dungeonmania.entities.movement.RandomMovementStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack, null);
    }

    @Override
    public void move(Game game) {
        determineMovementStrategy(game);
        Position nextPos = getMovementStrategy().getNewPosition(this, game, getPosition());
        game.getMap().moveTo(this, nextPos);
    }

    protected void determineMovementStrategy(Game game) {
        GameMap map = game.getMap();
        if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            // Invincibility Potion Movement
            setMovementStrategy(new InvincibilityMovementStrategy());

        } else {
           // Random Movement
           setMovementStrategy(new RandomMovementStrategy());
        }
    }

}
