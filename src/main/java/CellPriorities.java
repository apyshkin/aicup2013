import model.World;

public class CellPriorities {
  private final int[][][] priorities;

  public CellPriorities(Environment environment, int[][][] priorities) {
    World world = environment.getWorld();
    assert(priorities.length == world.getWidth());
    assert(priorities[0].length == world.getHeight());
    assert(priorities[0][0].length == 3);

    this.priorities = priorities;
  }

  public int getPriorityAtCell(int x, int y, int stance) {
    return priorities[x][y][stance];
  }
}
