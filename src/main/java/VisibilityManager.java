import model.TrooperStance;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 4:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class VisibilityManager {
  private World world;
  private CellChecker cellChecker;
  private int[][][] exposure;

  public VisibilityManager(World world, CellChecker cellChecker) {
    this.world = world;
    this.cellChecker = cellChecker;
    countVisibilities();
  }

  private void countVisibilities() {
    int n = world.getWidth();
    int m = world.getHeight();
    exposure = new int[n][m][3];
    for (int i = 0; i < n >> 1; ++i)
      for (int j = 0; j < m >> 1; ++j)
        if (cellChecker.cellIsFree(i, j))
          for (int x = 0; x < n; ++x)
            for (int y = 0; y < m; ++y)
              if (cellChecker.cellIsFree(x, y))
                for (int stance = 0; stance < 3; ++stance)
                  exposure[x][y][stance] += isVisibleFrom(i, j, x, y, Utils.getStance(stance)) ? 1 : 0;
  }

  public int getExposure(TrooperModel trooper, int x, int y) {
    if (x >= world.getWidth() >> 1)
      x = world.getWidth() - 1 - x;
    if (y >= world.getHeight() >> 1)
      y = world.getHeight() - 1 - y;
    return exposure[x][y][trooper.getStance().ordinal()];
  }

  public boolean isVisibleFrom(int x, int y, TrooperModel trooper) {
    int x1 = trooper.getX();
    int y1 = trooper.getY();
    return isVisibleFrom(x, y, x1, y1, trooper.getStance());
  }

  private boolean isVisibleFrom(int x, int y, int x1, int y1, TrooperStance stance) {
    return isRangeVisible(8, x, y, TrooperStance.STANDING, x1, y1, stance);
  }

  public boolean isVisibleBy(TrooperModel trooper, int x, int y) {
    int x1 = trooper.getX();
    int y1 = trooper.getY();
    return isRangeVisible(trooper.getVisionRange(), x1, y1, trooper.getStance(), x, y, TrooperStance.STANDING);
  }

  public boolean isVisible(TrooperModel trooper, int x, int y, TrooperStance stance) {
    return isRangeVisible(trooper.getVisionRange(), trooper.getX(), trooper.getY(), trooper.getStance(),
            x, y, stance);
  }

  public boolean isVisible(TrooperModel trooper, TrooperModel anotherTrooper) {
    return isRangeVisible(trooper.getVisionRange(), trooper.getX(), trooper.getY(), trooper.getStance(),
            anotherTrooper.getX(), anotherTrooper.getY(), anotherTrooper.getStance());
  }

  public boolean isAbleToShoot(TrooperModel trooper, TrooperModel anotherTrooper) {
    return isRangeVisible(trooper.getShootingRange(), trooper.getX(), trooper.getY(), trooper.getStance(),
            anotherTrooper.getX(), anotherTrooper.getY(), anotherTrooper.getStance());
  }

  public boolean isRangeVisible(double range, int x, int y, TrooperStance stance, int x1, int y1, TrooperStance stance1) {
    return world.isVisible(range, x, y, stance, x1, y1, stance1);
  }

}
