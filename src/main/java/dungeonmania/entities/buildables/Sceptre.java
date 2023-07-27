package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.buildables.recipes.AndRecipe;
import dungeonmania.entities.buildables.recipes.ItemRecipe;
import dungeonmania.entities.buildables.recipes.OrRecipe;
import dungeonmania.entities.buildables.recipes.Recipe;
import dungeonmania.entities.buildables.recipes.ReplacedItemRecipe;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Sceptre extends Buildable {
  public static final Recipe RECIPE = buildRecipe();
  private int durability;

  public Sceptre(int durability) {
    super(null, RECIPE);
    this.durability = durability;
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
    // TODO This doesnt apply a buff but it can be used? (Second aprt might be a lie.)
    // Either way we have to add more interfaces.
    return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 0, 1));
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
    Recipe recipe = new AndRecipe(new OrRecipe(new ItemRecipe(Wood.class, 1), new ItemRecipe(Arrow.class, 2)),
        new OrRecipe(new ReplacedItemRecipe(SunStone.class, 1, 2),
            new AndRecipe(new OrRecipe(new ItemRecipe(Key.class, 1), new ItemRecipe(Treasure.class, 1)),
                new ItemRecipe(SunStone.class, 1))));

    return recipe;
  }
}
