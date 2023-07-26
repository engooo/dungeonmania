package dungeonmania.entities.buildables;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.buildables.recipes.Recipe;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem, BattleItem {
  private Recipe recipe;

  public Buildable(Position position, Recipe recipe) {
    super(position);
    this.recipe = recipe;
  }

  public boolean canBuild(Inventory inventory) {
    return recipe.canBuild(inventory);
  }

  public void removeRecipeItems(Inventory inventory) {
    recipe.removeRecipeItems(inventory);
  }
}
