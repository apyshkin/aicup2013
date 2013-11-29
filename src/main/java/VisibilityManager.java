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
  private byte[][][][][] visibilityFrom; // a player from (i, j) in a standing stance can see a player in (x, y) in a stance z
  private int[][][] exposure;

  public VisibilityManager(World world, CellChecker cellChecker) {
    this.world = world;
    this.cellChecker = cellChecker;
    countVisibilities();
  }

  private void countVisibilities() {
    int n = world.getWidth();
    int m = world.getHeight();
    visibilityFrom = new byte[n][m][n][m][3];
    exposure = new int[n][m][3];
    for (int i = 0; i < n >> 1; ++i)
      for (int j = 0; j < m >> 1; ++j)
        if (cellChecker.cellIsFree(i, j))
          for (int x = 0; x < n; ++x)
            for (int y = 0; y < m; ++y)
              if (cellChecker.cellIsFree(x, y)) {
//                visibilityFrom[i][j][x][y][0] = world.isVisible(7, i, j, TrooperStance.STANDING,
//                        x, y, TrooperStance.PRONE) ? (byte) 1 : (byte) 0;
//                visibilityFrom[i][j][x][y][1] = world.isVisible(7, i, j, TrooperStance.STANDING,
//                        x, y, TrooperStance.KNEELING) ? (byte) 1 : (byte) 0;
//                visibilityFrom[i][j][x][y][2] = world.isVisible(7, i, j, TrooperStance.STANDING,
//                        x, y, TrooperStance.STANDING) ? (byte) 1 : (byte) 0;
                exposure[x][y][0] += visibilityFrom[i][j][x][y][0];
                exposure[x][y][1] += visibilityFrom[i][j][x][y][1];
                exposure[x][y][2] += visibilityFrom[i][j][x][y][2];
              }
  }

  public int getExposure(TrooperModel trooper, int x, int y) {
    if (x >= world.getWidth() >> 1)
      x = world.getWidth() - 1 - x;
    if (y >= world.getHeight() >> 1)
      y = world.getHeight() - 1 - y;
    return exposure[x][y][trooper.getStance().ordinal()];
  }

  public boolean isVisibleFrom(int x, int y, TrooperModel trooper) {
    int n = world.getWidth();
    int m = world.getHeight();
    int x1 = trooper.getX();
    int y1 = trooper.getY();
    if (x >= n >> 1) {
      x = n - 1 - x;
      x1 = n - 1 - x1;
    }
    if (y >= m >> 1) {
      y = m - 1 - y;
      y1 = m - 1 - y1;
    }
//    return visibilityFrom[x][y][x1][y1][trooper.getStance().ordinal()] == 1;
    return world.isVisible(7, x, y, TrooperStance.STANDING, x1, y1, trooper.getStance());
  }
}
