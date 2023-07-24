package dungeonmania.entities.buildables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Bow extends Buildable {
    public static final List<Recipe> RECIPES = buildRecipes();
    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
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
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 2, 1));
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
        List<Recipe> recipes = new ArrayList<>();

        Map<Class<? extends InventoryItem>, Integer> recipe = new HashMap<>();
        recipe.put(Wood.class, 1);
        recipe.put(Arrow.class, 3);
        recipes.add(new Recipe(recipe));

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
