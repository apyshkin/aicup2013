import model.World;

public class CellPriorities {
  private final Environment environment;
  private final int[][] priorities;

  public CellPriorities(Environment environment, int[][] priorities) {
    this.environment = environment;
    World world = environment.getWorld();
    assert(priorities.length == world.getWidth());
    assert(priorities[0].length == world.getHeight());

    this.priorities = new int[world.getWidth()][world.getHeight()];
    for (int i = 0; i < priorities.length; ++i) {
      System.arraycopy(priorities[i], 0, this.priorities[i], 0, priorities[i].length);
    }
  }

  public int getPriorityAtCell(int x, int y) {
    return priorities[x][y];
  }
}
