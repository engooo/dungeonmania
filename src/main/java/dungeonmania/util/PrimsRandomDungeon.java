package dungeonmania.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class PrimsRandomDungeon {
  private static final boolean WALL = false;
  private static final boolean EMPTY = true;

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
        maze[x][y] = WALL;
      }
    }
  }

  private void optionsAdd(Set<Position> positions) {
    options.addAll(positions);
  }

  private Position getRandomOption() {
    int randNum = nextRandom(options);
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

  private boolean validCoord(int x, int y) {
    return x >= 0 && x < width && y >= 0 && y < height;
  }

  private Set<Position> getCardinallyAdjacentWalls(Position pos) {
    Set<Position> positions = new HashSet<>();
    for (Position p : pos.getCardinallyAdjacentPositions()) {
      int x = p.getX();
      int y = p.getY();
      if (maze[x][y] == WALL && validCoord(x, y)) {
        positions.add(p);
      }
    }
    return positions;
  }

  private Set<Position> getNeighboursOf(Position pos) {
    Set<Position> neighbours = new HashSet<>();
    Set<Position> oneAway = new HashSet<>(getCardinallyAdjacentWalls(pos));

    for (Position p : oneAway) {
      neighbours.addAll(getCardinallyAdjacentWalls(p));
    }
    for (Position p : oneAway) {
      if (neighbours.contains(p))
        neighbours.remove(p);
    }

    return neighbours;
  }

  private int nextRandom(Set<?> limit) {
    return random.nextInt(limit.size()) - 1;
  }

  public boolean[][] generateNewDungeon() {
    clearDungeon();

    // Set initial position to empty
    maze[start.getX()][start.getY()] = EMPTY;

    // Add to options all neighbours of 'start' not on boundary that are of distance 2 away and are walls
    optionsAdd(getNeighboursOf(start));

    while (!options.isEmpty()) {
      Position next = getRandomOption();

      Set<Position> neighbours = getNeighboursOf(next);
      if (!neighbours.isEmpty()) {
        int randNum = nextRandom(neighbours);
        Position neighbour = neighbours.stream().skip(randNum).findFirst().get();
        // maze[ next ] = empty (i.e. true)
        maze[next.getX()][next.getY()] = true;

        Position inBetween = new Position((next.getX() + neighbour.getX()) / 2, (next.getY() + neighbour.getY()) / 2);
        // maze[ position inbetween next and neighbour ] = empty (i.e. true)
        maze[inBetween.getX()][inBetween.getY()] = EMPTY;
        // maze[ neighbour ] = empty (i.e. true)
        maze[neighbour.getX()][neighbour.getY()] = EMPTY;

        optionsAdd(neighbours);
      }

      // At the end there is still a case where our end position isn't connected to the map
      // This will make it consistently have a pathway between the two.
      if (maze[end.getX()][end.getY()] == WALL) {
        maze[end.getX()][end.getY()] = EMPTY;
      }

      neighbours = new HashSet<>(end.getCardinallyAdjacentPositions());
      for (Position p : neighbours) {
        int x = p.getX();
        int y = p.getY();
        if (validCoord(x, y) && maze[x][y] == EMPTY) {
          // The maze is connected to the end.
          return maze;
        }
      }
      // The maze is not connected to the end so connect it.
      int randNum = nextRandom(neighbours);
      Position fix = neighbours.stream().skip(randNum).findFirst().get();
      maze[fix.getX()][fix.getY()] = EMPTY;
    }

    return maze;
  }

}
