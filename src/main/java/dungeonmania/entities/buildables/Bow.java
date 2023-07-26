package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.buildables.recipes.AndRecipe;
import dungeonmania.entities.buildables.recipes.ItemRecipe;
import dungeonmania.entities.buildables.recipes.OrRecipe;
import dungeonmania.entities.buildables.recipes.Recipe;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Bow extends Buildable {
  public static final Recipe RECIPE = buildRecipe();
  private int durability;

  public Bow(int durability) {
    super(null, RECIPE);
    this.durability = durability;
  }

  @Override
  public BattleStatistics applyBuff(BattleStatistics origin) {
    return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 2, 1));
  }

  @Override
  public int getDurability() {
    return durability;
  }

  @Override
  public void setDurability(int durability) {
    this.durability = durability;
  }

  private static Recipe buildRecipe() {
    Recipe recipe = new AndRecipe(new ItemRecipe(Wood.class, 1), new ItemRecipe(Arrow.class, 3));

    return recipe;
  }

}
