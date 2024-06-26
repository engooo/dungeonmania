package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.TreasureItem;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.movementListeners.ActionOnOverlap;
import dungeonmania.entities.playerState.BaseState;
import dungeonmania.entities.playerState.InvincibleState;
import dungeonmania.entities.playerState.InvisibleState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements Battleable, ActionOnOverlap {
  public static final double DEFAULT_ATTACK = 5.0;
  public static final double DEFAULT_HEALTH = 5.0;
  private BattleStatistics battleStatistics;
  private Inventory inventory;
  private Queue<Potion> potionQueue = new LinkedList<>();
  private Potion inEffective = null;
  private int nextTrigger = 0;

  private int collectedTreasureCount = 0;
  private int slainEnemyCount = 0;

  private PlayerState invincibleState;
  private PlayerState invisibleState;
  private PlayerState baseState;
  private PlayerState state;

  // Represents the current state of the GameMap after player has moved.
  // Null if player has not moved yet.
  private GameMap currMap = null;

  public Player(Position position, double health, double attack) {
    super(position);
    battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
        BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
    inventory = new Inventory();

    invincibleState = new InvincibleState(this);
    invisibleState = new InvisibleState(this);
    baseState = new BaseState(this);
    state = getBaseState();
  }

  public PlayerState getInvincibleState() {
    return invincibleState;
  }

  public PlayerState getInvisibleState() {
    return invisibleState;
  }

  public PlayerState getBaseState() {
    return baseState;
  }

  public void setState(PlayerState state) {
    this.state = state;
  }

  public int getCollectedTreasureCount() {
    return collectedTreasureCount;
  }

  public int getSlainEnemyCount() {
    return slainEnemyCount;
  }

  public void incrementSlainEnemyCount() {
    slainEnemyCount++;
  }

  public boolean hasWeapon() {
    return inventory.hasWeapon();
  }

  public BattleItem getWeapon() {
    return inventory.getWeapon();
  }

  public List<String> getBuildables() {
    return inventory.getBuildables(getCurrMap());
  }

  public boolean build(String entity, EntityFactory factory) {
    InventoryItem item = inventory.checkBuildCriteria(getCurrMap(), entity, factory);
    if (item == null)
      return false;
    return inventory.add(item);
  }

  public void move(GameMap map, Direction direction) {
    this.setFacing(direction);
    map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    // Map has been updated after movement.
    this.setCurrMap(map);
  }

  @Override
  public void onOverlap(GameMap map, Entity entity) {
    if (entity instanceof Enemy) {
      if (entity instanceof Mercenary) {
        if (((Mercenary) entity).alliedOrMindControl()) {
          return;
        }
      }
      map.getGame().battle(this, (Enemy) entity);
    }
  }

  @Override
  public boolean canMoveOnto(GameMap map, Entity entity) {
    return true;
  }

  public Entity getEntity(String itemUsedId) {
    return inventory.getEntity(itemUsedId);
  }

  public boolean pickUp(Entity item) {
    if (item instanceof TreasureItem)
      collectedTreasureCount++;

    if (item instanceof InventoryItem) {
      if (!((item instanceof Key) && inventory.count(Key.class) >= 1)) {
        return inventory.add((InventoryItem) item);
      }
    }
    return false;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public Potion getEffectivePotion() {
    return inEffective;
  }

  public <T extends InventoryItem> void use(Class<T> itemType) {
    T item = inventory.getFirst(itemType);
    if (item != null)
      inventory.remove(item);
  }

  public void use(Bomb bomb, GameMap map) {
    inventory.remove(bomb);
    bomb.onPutDown(map, getPosition());
  }

  public void triggerNext(int currentTick) {
    inEffective = potionQueue.poll();
    state.transition(inEffective);
    if (inEffective != null)
      nextTrigger = currentTick + inEffective.getDuration();
  }

  public void changeState(PlayerState playerState) {
    state = playerState;
  }

  public void use(Potion potion, int tick) {
    inventory.remove(potion);
    potionQueue.add(potion);
    if (inEffective == null) {
      triggerNext(tick);
    }
  }

  public void onTick(int tick) {
    if (inEffective == null || tick == nextTrigger) {
      triggerNext(tick);
    }
  }

  public void remove(InventoryItem item) {
    inventory.remove(item);
  }

  @Override
  public BattleStatistics getBattleStatistics() {
    return battleStatistics;
  }

  public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
    return inventory.count(itemType);
  }

  public BattleStatistics applyBuff(BattleStatistics origin) {
    return state.applyBuff(origin);
  }

  private GameMap getCurrMap() {
    return currMap;
  }

  private void setCurrMap(GameMap currMap) {
    this.currMap = currMap;
  }

  /**
   * @pre hasItem(Sceptre) returned true
   */
  public int getMindControlDuration() {
    return inventory.getFirst(Sceptre.class).getDuration();
  }

  public <T extends InventoryItem> boolean hasItem(Class<T> itemType) {
    return !inventory.getEntities(itemType).isEmpty();
  }

}
