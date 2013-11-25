import model.TrooperStance;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttackTactics implements ITactics {

  private final Environment environment;

  public AttackTactics(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setAction(ITrooperStrategy trooper) {
    trooper.setActionUnderTactics(this);
  }

  @Override
  public CellPriorities generateCellPriorities(TrooperModel trooper) {
    World world = environment.getWorld();
    int[][] priorities = new int[world.getWidth()][world.getHeight()];
//    for (int i = 0; i < world.getWidth(); ++i)
//      for (int j = 0; j < world.getHeight(); ++j) {
//        for (int k = 0; k < world.getWidth(); ++k)
//          for (int l = 0; l < world.getHeight(); ++l)
//            if (world.isVisible(trooper.getVisionRange(), k, l, TrooperStance.STANDING, i, j, TrooperStance.STANDING))
//              ++priorities[i][j];
//      }
//
//    for (int i = 0; i < world.getWidth(); ++i) {
//      for (int j = 0; j < world.getHeight(); ++j)
//        System.out.print(priorities[i][j] + " ");
//      System.out.println();
//    }

    return new CellPriorities(environment, priorities);
  }
}

