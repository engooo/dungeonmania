package dungeonmania.entities.buildables;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.buildables.recipes.Recipe;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem, BattleItem {
  private Recipe recipe;

  public Buildable(Position position, Recipe recipe) {
    super(position);
    this.recipe = recipe;
  }

  // Game is passed in for Overwritten subclass methods.
  // Currently just MidnightArmour requires it, but who's to say there couldnt be more.
  // I'm future planning, not having redundant parameters.
  public boolean canBuild(GameMap map, Inventory inventory) {
    return recipe.canBuild(inventory);
  }

  public void removeRecipeItems(Inventory inventory) {
    recipe.removeRecipeItems(inventory);
  }
}
