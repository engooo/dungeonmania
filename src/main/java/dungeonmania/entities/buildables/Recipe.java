package dungeonmania.entities.buildables;

import java.util.Map;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Recipe {
	private Map<Class<? extends InventoryItem>, Integer> recipeMap;

	public Recipe(Map<Class<? extends InventoryItem>, Integer> recipeMap) {
		this.recipeMap = recipeMap;
	}

	public Map<Class<? extends InventoryItem>, Integer> getRecipeMap() {
		return recipeMap;
	}

	/**
	 * Determine if a players inventory has sufficient materials to build this
	 * recipe.
	 */
	public boolean canBuild(Inventory inventory) {
		return recipeMap.entrySet().stream().allMatch(entry -> {
			Class<? extends InventoryItem> material = entry.getKey();
			int amount = entry.getValue();
			return inventory.getEntities(material).size() >= amount;
		});
	}

	/**
	 * Function assumes that canBuild was previously called and returned true.
	 */
	public void removeRecipeItems(Inventory inventory) {
		recipeMap.forEach((material, amount) -> {
			for (int removed = 0; removed < amount; removed++) {
				inventory.remove(inventory.getFirst(material));
			}
		});
	}
}
