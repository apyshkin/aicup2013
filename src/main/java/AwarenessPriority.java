import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class AwarenessPriority implements IPriority {
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][] awareness;

  public AwarenessPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    init();
  }

  private void init() {
    BattleMap battleMap = environment.getBattleMap();
    awareness = new int[battleMap.getWidth()][battleMap.getHeight()];
    int curX = trooper.getX();
    int curY = trooper.getY();
    int maxMovesNumber = trooper.countMaxMoves();
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j)
        if (battleMap.getCellChecker().cellIsFree(i, j)) {
          int dist = Math.abs(i - curX) + Math.abs(j - curY);
          if (dist > maxMovesNumber)
           awareness[i][j] += dist;
        }
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    return awareness[x][y];
  }
}
