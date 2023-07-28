package dungeonmania;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import dungeonmania.util.PrimsRandomDungeon;

/**
 * DO NOT CHANGE METHOD SIGNITURES OF THIS FILE
 * */
public class DungeonManiaController {
  private Game game = null;

  public String getSkin() {
    return "default";
  }

  public String getLocalisation() {
    return "en_US";
  }

  /**
   * /dungeons
   */
  public static List<String> dungeons() {
    return FileLoader.listFileNamesInResourceDirectory("dungeons");
  }

  /**
   * /configs
   */
  public static List<String> configs() {
    return FileLoader.listFileNamesInResourceDirectory("configs");
  }

  /**
   * /game/new
   */
  public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
    if (!dungeonName.startsWith("random_") && !dungeons().contains(dungeonName)) {
      throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
    }

    if (!configs().contains(configName)) {
      throw new IllegalArgumentException(configName + " is not a configuration that exists");
    }

    try {
      GameBuilder builder = new GameBuilder();
      game = builder.setConfigName(configName).setDungeonName(dungeonName).buildGame();
      return ResponseBuilder.getDungeonResponse(game);
    } catch (JSONException e) {
      return null;
    }
  }

  /**
   * /game/dungeonResponseModel
   */
  public DungeonResponse getDungeonResponseModel() {
    return null;
  }

  /**
   * /game/tick/item
   */
  public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
    return ResponseBuilder.getDungeonResponse(game.tick(itemUsedId));
  }

  /**
   * /game/tick/movement
   */
  public DungeonResponse tick(Direction movementDirection) {
    return ResponseBuilder.getDungeonResponse(game.tick(movementDirection));
  }

  /**
   * /game/build
   */
  public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
    List<String> validBuildables = List.of("bow", "shield", "midnight_armour", "sceptre");
    if (!validBuildables.contains(buildable)) {
      throw new IllegalArgumentException("Only bow, shield, midnight_armour and sceptre can be built");
    }

    return ResponseBuilder.getDungeonResponse(game.build(buildable));
  }

  /**
   * /game/interact
   */
  public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
    return ResponseBuilder.getDungeonResponse(game.interact(entityId));
  }

  /**
     * /game/new/generate
     */
  public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName)
      throws IllegalArgumentException {
    Position start = new Position(0, 0);
    Position end = new Position(xEnd - xStart, yEnd - yStart);
    int width = end.getX();
    int height = end.getY();

    PrimsRandomDungeon randomGen = new PrimsRandomDungeon(width, height, start, end);
    boolean[][] dungeon = randomGen.generateNewDungeon();
    String dungeonName = generateDungeonFileFromBoolArray(dungeon, height, width, start, end);

    return newGame(dungeonName, configName);
  }

  /**
   * /game/rewind
   */
  public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
    return null;
  }

  private String generateDungeonFileFromBoolArray(boolean[][] map, int height, int width, Position start,
      Position end) {

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

    //int mapHash = map.hashCode();
    //String fileName = "random_" + mapHash;
    String fileName = "random_" + "map";

    String path = "../../../resources/dungeons_random/" + fileName;
    try {
      FileWriter fileWriter = new FileWriter(path);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(dungeon.toString(width));
      fileWriter.close();
    } catch (Exception e) {
      System.out.println("Random Gen Failure");
      e.printStackTrace();
    }

    return fileName;
  }

  private static JSONObject jsonEntity(String type, int x, int y) {
    JSONObject obj = new JSONObject();
    obj.put("type", type);
    obj.put("x", x);
    obj.put("y", y);

    return obj;
  }

}
