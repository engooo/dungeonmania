package dungeonmania.entities.buildables.recipes;

import dungeonmania.entities.inventory.Inventory;

public class AndRecipe extends Recipe {
  private Recipe left;
  private Recipe right;

  public AndRecipe(Recipe left, Recipe right) {
    this.left = left;
    this.right = right;
  }

  public boolean canBuild(Inventory inventory) {
    return left.canBuild(inventory) && right.canBuild(inventory);
  }

  public void removeRecipeItems(Inventory inventory) {
    left.removeRecipeItems(inventory);
    right.removeRecipeItems(inventory);
  }
}
