package dungeonmania.entities.buildables.recipes;

import dungeonmania.entities.inventory.Inventory;

public class OrRecipe extends Recipe {
  private Recipe left;
  private Recipe right;

  public OrRecipe(Recipe left, Recipe right) {
    this.left = left;
    this.right = right;
  }

  public boolean canBuild(Inventory inventory) {
    return left.canBuild(inventory) || right.canBuild(inventory);
  }

  public void removeRecipeItems(Inventory inventory) {
    if (left.canBuild(inventory)) {
      left.removeRecipeItems(inventory);
    } else if (right.canBuild(inventory)) {
      right.removeRecipeItems(inventory);
    }
  }
}
