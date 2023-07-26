package dungeonmania.entities.buildables.recipes;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class StaticItemRecipe extends Recipe {
  private Class<? extends InventoryItem> item;
  private int amount;

  public StaticItemRecipe(Class<? extends InventoryItem> item, int amount) {
    this.item = item;
    this.amount = amount;
  }

  public boolean canBuild(Inventory inventory) {
    return inventory.getEntities(item).size() >= amount;
  }

  public void removeRecipeItems(Inventory inventory) {
    // A static item should not get consumed upon use.
    return;
  }
}
