import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatrolTactics implements ITactics {
  private final Environment environment;

  public PatrolTactics(Environment environment) {
    this.environment = environment;
  }
  @Override
  public void setAction(ITrooperStrategy trooper) {
    trooper.setActionUnderTactics(this);
  }

  @Override
  public CellPriorities generateCellPriorities() {
    World world = environment.getWorld();
    int[][] priorities = new int[world.getWidth()][world.getHeight()];
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        priorities[i][j] = i + j;

    return new CellPriorities(environment, priorities);
  }
}
