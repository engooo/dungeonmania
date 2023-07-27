package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.movement.AlliedMercenaryMovementStrategy;
import dungeonmania.entities.movement.FollowHostileMovementStrategy;
import dungeonmania.entities.movement.InvincibilityMovementStrategy;
import dungeonmania.entities.movement.RandomMovementStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
  public static final int DEFAULT_BRIBE_AMOUNT = 1;
  public static final int DEFAULT_BRIBE_RADIUS = 1;
  public static final double DEFAULT_ATTACK = 5.0;
  public static final double DEFAULT_HEALTH = 10.0;

  private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
  private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

  private double allyAttack;
  private double allyDefence;
  private boolean allied = false;
  private int mindControl = 0;
  private boolean isAdjacentToPlayer = false;

  public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius, double allyAttack,
      double allyDefence) {
    super(position, health, attack, null);
    this.bribeAmount = bribeAmount;
    this.bribeRadius = bribeRadius;
    this.allyAttack = allyAttack;
    this.allyDefence = allyDefence;
  }

  public boolean isAllied() {
    return allied;
  }

  public boolean isAdjacentToPlayer() {
    return isAdjacentToPlayer;
  }

  @Override
  public void onOverlap(GameMap map, Entity entity) {
    if (alliedOrMindControl()) {
      return;
    }
    super.onOverlap(map, entity);
  }

  /**
   * check whether the current merc can be bribed
   *
   * @param player
   * @return
   */
  private boolean canBeBribed(Player player) {
    return !alliedOrMindControl() && (bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount)
        || player.hasItem(Sceptre.class);
  }

  /**
   * bribe the merc
   */
  private void bribe(Player player) {
    allied = true;
    for (int i = 0; i < bribeAmount; i++) {
      player.use(Treasure.class);
    }

  }

  private void mindControl(Player player) {
    mindControl = player.getMindControlDuration();
    player.use(Sceptre.class);
  }

  @Override
  public void interact(Player player, Game game) {
    if (player.hasItem(Sceptre.class)) {
      mindControl(player);
    } else {
      bribe(player);
    }
    if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
      isAdjacentToPlayer = true;
  }

  @Override
  public void move(Game game) {
    determineMovementStrategy(game);
    mindControl--;

    GameMap map = game.getMap();
    Player player = game.getPlayer();

    Position nextPos = getMovementStrategy().getNewPosition(this, game, getPosition());
    if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos)) {
      isAdjacentToPlayer = true;
    }

    map.moveTo(this, nextPos);
  }

  @Override
  public boolean isInteractable(Player player) {
    return !alliedOrMindControl() && canBeBribed(player);
  }

  @Override
  public BattleStatistics getBattleStatistics() {
    if (!alliedOrMindControl())
      return super.getBattleStatistics();
    return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
  }

  @Override
  protected void determineMovementStrategy(Game game) {
    GameMap map = game.getMap();

    if (alliedOrMindControl()) {
      // Allied Mercenary Movement
      this.setMovementStrategy(new AlliedMercenaryMovementStrategy());

    } else if (map.getPlayer().getEffectivePotion() instanceof InvisibilityPotion) {
      // Random Movement
      this.setMovementStrategy(new RandomMovementStrategy());

    } else if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
      // Invincibility Potion Movement
      this.setMovementStrategy(new InvincibilityMovementStrategy());

    } else {
      // Follow hostile
      this.setMovementStrategy(new FollowHostileMovementStrategy());
    }
  }

  private boolean alliedOrMindControl() {
    return allied || mindControl > 0;
  }
}
