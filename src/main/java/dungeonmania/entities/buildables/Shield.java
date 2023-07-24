package dungeonmania.entities.buildables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Shield extends Buildable {
    public static final List<Recipe> RECIPES = buildRecipes();
    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, defence, 1, 1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public List<Recipe> getRecipes() {
        return RECIPES;
    }

    private static List<Recipe> buildRecipes() {
        // This implementation would word better if recipes were saved in the config
        // file and could be read into the list. Since I don't think we can touch that
        // file, building each recipe like this still works.
        List<Recipe> recipes = new ArrayList<>();

        Map<Class<? extends InventoryItem>, Integer> recipe1 = new HashMap<>();
        recipe1.put(Wood.class, 2);
        recipe1.put(Treasure.class, 1);
        recipes.add(new Recipe(recipe1));

        Map<Class<? extends InventoryItem>, Integer> recipe2 = new HashMap<>();
        recipe2.put(Wood.class, 2);
        recipe2.put(Key.class, 1);
        recipes.add(new Recipe(recipe2));

        return recipes;
    }

    public static boolean canBuild(Inventory inventory) {
        for (Recipe recipe : RECIPES) {
            if (recipe.canBuild(inventory)) {
                return true;
            }
        }
        return false;
    }
}
