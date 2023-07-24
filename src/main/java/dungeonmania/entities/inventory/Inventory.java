package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.buildables.Recipe;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Sword;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {
        // This could probably be improved becuase its still hard coded for each entity
        // type, but it's better than before.
        List<String> result = new ArrayList<>();

        if (Bow.canBuild(this)) {
            result.add("bow");
        }
        if (Shield.canBuild(this)) {
            result.add("shield");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(String buildable, EntityFactory factory) {
        Buildable item = factory.createBuildable(buildable);
        List<Recipe> recipes = item.getRecipes();

        // Check each recipe and determine if the player has can build it. If it can be
        // built, remove items from the players inventory and return the built entity.
        for (Recipe recipe : recipes) {
            if (recipe.canBuild(this)) {
                recipe.removeRecipeItems(this);
                return item;
            }
        }
        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

}
