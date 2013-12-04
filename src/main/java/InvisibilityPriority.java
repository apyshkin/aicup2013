import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvisibilityPriority implements IPriority {
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][][] visibilities;

  public InvisibilityPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    init();
  }

  private void init() {
    BattleMap battleMap = environment.getBattleMap();
    visibilities = new int[battleMap.getWidth()][battleMap.getHeight()][3];
    ArrayList <TrooperModel> enemies = environment.getEnemies();
    TrooperMemorizer memorizer = new TrooperMemorizer(trooper);
    memorizer.memorize();
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j)
        if (battleMap.getCellChecker().cellIsFree(i, j)) {
          for (int s = 0; s < 3; ++s) {
            trooper.move(i, j, Utils.getStance(s));
            for (TrooperModel enemy : enemies) {
              if (battleMap.isVisible(enemy, trooper))
                ++visibilities[i][j][s];
            }
          }
        }
    memorizer.reset();
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    return visibilities[x][y][stance];
  }
}
