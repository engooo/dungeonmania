package dungeonmania.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class PrimsRandomDungeon {
  private int width;
  private int height;
  private Position start;
  private Position end;

  private boolean[][] maze;
  private Set<Position> options = new HashSet<>();
  private Random random;

  public PrimsRandomDungeon(int width, int height, Position start, Position end) {
    this.width = width;
    this.height = height;
    this.start = start;
    this.end = end;

    this.random = new Random();
  }

  private void clearDungeon() {
    // Initalize maze and set all to false
    this.maze = new boolean[height][width];
    // False represents walls, true represents empty spaces
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        maze[y][x] = false;
      }
    }
  }

  private void optionsAdd(Set<Position> positions) {
    options.addAll(positions);
  }

  private Position getRandomOption() {
    int randNum = random.nextInt(options.size() - 1);
    Iterator<Position> iterator = options.iterator();
    Position option = null;
    for (int i = 0; iterator.hasNext(); i++) {
      option = iterator.next();
      if (i == randNum) {
        iterator.remove();
      }
    }
    return option;
  }

  private Set<Position> getNeighboursOf(Position pos) {
    Set<Position> neighbours = new HashSet<>();
    // TODO ew
    return neighbours;
  }

  public boolean[][] generateNewDungeon() {
    clearDungeon();

    // Set initial position to empty
    maze[start.getX()][start.getY()] = true;

    // Add to options all neighbours of 'start' not on boundary that are of distance 2 away and are walls
    optionsAdd(getNeighboursOf(start));

    while (!options.isEmpty()) {
      Position next = getRandomOption();

      Set<Position> neighbours = getNeighboursOf(next);
      if (!neighbours.isEmpty()) {
        int randNum = random.nextInt(neighbours.size() - 1);
        Position neighbour = neighbours.stream().skip(random.nextInt(randNum)).findFirst().get();
        // maze[ next ] = empty (i.e. true)
        maze[next.getX()][next.getY()] = true;

        Position inBetween = new Position((next.getX() + neighbour.getX()) / 2, (next.getY() + neighbour.getY()) / 2);
        // maze[ position inbetween next and neighbour ] = empty (i.e. true)
        maze[inBetween.getX()][inBetween.getY()] = true;
        // maze[ neighbour ] = empty (i.e. true)
        maze[neighbour.getX()][neighbour.getY()] = true;

        optionsAdd(neighbours);
      }
    }

    // # at the end there is still a case where our end position isn't connected to the map
    // # we don't necessarily need this, you can just keep randomly generating maps (was original intention)
    // # but this will make it consistently have a pathway between the two.
    // # if maze[end] is a wall:
    //     maze[end] = empty

    //     let neighbours = neighbours not on boundary of distance 1 from maze[end]
    //     if there are no cells in neighbours that are empty:
    //         # let's connect it to the grid
    //         let neighbour = random from neighbours
    //         maze[neighbour] = empty

    return maze;
  }

}
