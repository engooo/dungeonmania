package dungeonmania.entities.buildables.recipes;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class ItemRecipe extends Recipe {
  private Class<? extends InventoryItem> item;
  private int amount;

  public ItemRecipe(Class<? extends InventoryItem> item, int amount) {
    this.item = item;
    this.amount = amount;
  }

  public boolean canBuild(Inventory inventory) {
    return inventory.getEntities(item).size() >= amount;
  }

  public void removeRecipeItems(Inventory inventory) {
    for (int removed = 0; removed < amount; removed++) {
      inventory.remove(inventory.getFirst(item));
    }
  }
}
