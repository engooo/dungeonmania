package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.buildables.recipes.AndRecipe;
import dungeonmania.entities.buildables.recipes.ItemRecipe;
import dungeonmania.entities.buildables.recipes.Recipe;
import dungeonmania.entities.buildables.recipes.StaticItemRecipe;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.map.GameMap;

public class MidnightArmour extends Buildable {
  public static final Recipe RECIPE = buildRecipe();
  private double defence;
  private double attack;

  public MidnightArmour(double defence, double attack) {
    super(null, RECIPE);
    this.defence = defence;
    this.attack = attack;
  }

  @Override
  public void use(Game game) {
    return;
  }

  @Override
  public BattleStatistics applyBuff(BattleStatistics origin) {
    return BattleStatistics.applyBuff(origin, new BattleStatistics(0, attack, defence, 1, 1));
  }

  @Override
  public int getDurability() {
    return 1;
  }

  @Override
  public void setDurability(int durability) {
  }

  @Override
  public boolean canBuild(GameMap map, Inventory inventory) {
    if (map == null || map.getEntities(ZombieToast.class).size() > 0) {
      return false;
    }

    return super.canBuild(map, inventory);
  }

  private static Recipe buildRecipe() {
    Recipe recipe = new AndRecipe(new ItemRecipe(Sword.class, 1), new StaticItemRecipe(SunStone.class, 1));

    return recipe;
  }

}
