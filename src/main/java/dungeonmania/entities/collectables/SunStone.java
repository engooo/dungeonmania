package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

public class SunStone extends CollectableEntity {
  // This class needs to extend treasure, but that stuffs up the recipe.
  // Future problem, find a way around this. Probably inheritance?
  public SunStone(Position position) {
    super(position);
  }
}
