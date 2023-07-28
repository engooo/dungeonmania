package dungeonmania;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalFactory;
import dungeonmania.map.GameMap;
import dungeonmania.map.GraphNode;
import dungeonmania.map.GraphNodeFactory;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import dungeonmania.util.PrimsRandomDungeon;

/**
 * GameBuilder -- A builder to build up the whole game
 * @author      Webster Zhang
 * @author      Tina Ji
 */
public class GameBuilder {
  private String configName;
  private String dungeonName;

  private JSONObject config;
  private JSONObject dungeon;

  public GameBuilder setConfigName(String configName) {
    this.configName = configName;
    return this;
  }

  public GameBuilder setDungeonName(String dungeonName) {
    this.dungeonName = dungeonName;
    return this;
  }

  public Game buildGame() {
    loadConfig();
    loadDungeon();
    if (dungeon == null && config == null) {
      return null; // something went wrong
    }

    Game game = new Game(dungeonName);
    EntityFactory factory = new EntityFactory(config);
    game.setEntityFactory(factory);
    buildMap(game);
    buildGoals(game);
    game.init();

    return game;
  }

  private void loadConfig() {
    String configFile = String.format("/configs/%s.json", configName);
    try {
      config = new JSONObject(FileLoader.loadResourceFile(configFile));
    } catch (IOException e) {
      e.printStackTrace();
      config = null;
    }
  }

  private void loadDungeon() {
    String dungeonFile = String.format("/dungeons/%s.json", dungeonName);
    try {
      dungeon = new JSONObject(FileLoader.loadResourceFile(dungeonFile));
    } catch (IOException e) {
      dungeon = null;
    }
  }

  private void buildMap(Game game) {
    GameMap map = new GameMap();
    map.setGame(game);

    dungeon.getJSONArray("entities").forEach(e -> {
      JSONObject jsonEntity = (JSONObject) e;
      GraphNode newNode = GraphNodeFactory.createEntity(jsonEntity, game.getEntityFactory());
      Entity entity = newNode.getEntities().get(0);

      if (newNode != null) {
        map.addNode(newNode);
      }

      if (entity instanceof Player)
        map.setPlayer((Player) entity);
    });
    game.setMap(map);
  }

  public void buildGoals(Game game) {
    if (!dungeon.isNull("goal-condition")) {
      Goal goal = GoalFactory.createGoal(dungeon.getJSONObject("goal-condition"), config);
      game.setGoals(goal);
    }
  }

  public void generateDungeon(int height, int width, Position start, Position end) throws IllegalArgumentException {

    PrimsRandomDungeon randomGen = new PrimsRandomDungeon(width, height, start, end);
    boolean[][] dungeon = randomGen.generateNewDungeon();
    JSONObject jsonDungeon = generateDungeonJSON(dungeon, height, width, start, end);
    this.dungeon = jsonDungeon;
  }

  private JSONObject generateDungeonJSON(boolean[][] map, int height, int width, Position start, Position end) {

    JSONObject dungeon = new JSONObject();
    JSONArray entities = new JSONArray();

    entities.put(jsonEntity("player", start.getX(), start.getY()));
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (!map[x][y]) {
          entities.put(jsonEntity("wall", x, y));
        }
      }
    }

    dungeon.put("entities", entities);
    dungeon.put("goal-condition", "exit");

    return dungeon;
  }

  private static JSONObject jsonEntity(String type, int x, int y) {
    JSONObject obj = new JSONObject();
    obj.put("type", type);
    obj.put("x", x);
    obj.put("y", y);

    return obj;
  }

  public Game buildGameRandom(int height, int width, Position start, Position end) {
    loadConfig();
    generateDungeon(height, width, start, end);

    if (dungeon == null && config == null) {
      return null; // something went wrong
    }

    Game game = new Game(dungeonName);
    EntityFactory factory = new EntityFactory(config);
    game.setEntityFactory(factory);
    buildMap(game);
    buildGoals(game);
    game.init();

    return game;
  }

}
