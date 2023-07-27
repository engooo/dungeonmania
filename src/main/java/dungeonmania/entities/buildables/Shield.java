package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.buildables.recipes.AndRecipe;
import dungeonmania.entities.buildables.recipes.ItemRecipe;
import dungeonmania.entities.buildables.recipes.OrRecipe;
import dungeonmania.entities.buildables.recipes.Recipe;
import dungeonmania.entities.buildables.recipes.StaticItemRecipe;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Shield extends Buildable {
  public static final Recipe RECIPE = buildRecipe();
  private int durability;
  private double defence;

  public Shield(int durability, double defence) {
    super(null, RECIPE);
    this.durability = durability;
    this.defence = defence;
  }

  @Override
  public void use(Game game) {
    durability--;
    if (durability <= 0) {
      game.getPlayer().remove(this);
    }
  }

  @Override
  public BattleStatistics applyBuff(BattleStatistics origin) {
    return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, defence, 1, 1));
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
    Recipe recipe = new AndRecipe(new ItemRecipe(Wood.class, 2), new OrRecipe(new StaticItemRecipe(SunStone.class, 1),
        new OrRecipe(new ItemRecipe(Treasure.class, 1), new ItemRecipe(Key.class, 1))));

    return recipe;
  }

}
