package dungeonmania.entities.buildables.recipes;

import dungeonmania.entities.inventory.Inventory;

public abstract class Recipe {
  /**
   * Determine if a players inventory has sufficient materials to build this
   * recipe.
   */
  public abstract boolean canBuild(Inventory inventory);

  /**
   * Function assumes that canBuild was previously called and returned true.
   */
  public abstract void removeRecipeItems(Inventory inventory);
}
