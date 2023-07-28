package dungeonmania.entities.buildables.recipes;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class ReplacedItemRecipe extends Recipe {
  private Class<? extends InventoryItem> item;
  private int destroy;
  private int all;

  public ReplacedItemRecipe(Class<? extends InventoryItem> item, int destroy, int all) {
    this.item = item;
    this.destroy = destroy;
    this.all = all;
  }

  public boolean canBuild(Inventory inventory) {
    return inventory.getEntities(item).size() >= all;
  }

  public void removeRecipeItems(Inventory inventory) {
    for (int removed = 0; removed < destroy; removed++) {
      inventory.remove(inventory.getFirst(item));
    }
  }
}
