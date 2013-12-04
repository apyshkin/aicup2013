/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class VisionPathPotential implements IPathPotential {
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][][] visibilities;
  private int coeff;

  public VisionPathPotential(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    init();
  }

  private void init() {
  }

  @Override
  public void preCount(boolean[][] reachableCells, int coeff) {
    final BattleMap battleMap = environment.getBattleMap();
    this.coeff = coeff;
    visibilities = new int[battleMap.getWidth()][battleMap.getHeight()][3];
    int curVisibility = countVisibility();
    TrooperMemorizer memorizer = new TrooperMemorizer(trooper);
    memorizer.memorize();
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j)
        for (int s = 0; s < 3; ++s) {
          if (reachableCells[i][j]) {
            trooper.move(i, j, Utils.getStance(s));
            visibilities[i][j][s] = coeff * (countVisibility() - curVisibility);
          }
        }

    memorizer.reset();
  }

  private int countVisibility() {
    int ans = 0;
    final BattleMap battleMap = environment.getBattleMap();
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j) {
        boolean visible = false;
        for (TrooperModel trooper : environment.getMyTroopers()) {
          if (environment.getBattleMap().isVisibleBy(trooper, i, j)) {
            visible = true;
            break;
          }
        }
        if (visible)
          ++ans;
      }
    return ans;
  }

  @Override
  public int getPotential(int x, int y, int stance) {
    return visibilities[x][y][stance];
  }

  @Override
  public int countNewPathPoints(int currentPathPotential, int newPositionPotential) {
    return Math.max(currentPathPotential, newPositionPotential);
  }

  @Override
  public String toString() {
    return "Potential for " + this.getClass().getSimpleName() + " : coefficient = " + coeff;
  }

}
