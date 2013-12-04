import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 2:55 PM * To change this template use File | Settings | File Templates.
 */
public class VisibleEnemyAnalysis implements IAnalysis {
  private final static Logger logger = Logger.getLogger(VisibleEnemyAnalysis.class.getName());
  private final Environment environment;

  public VisibleEnemyAnalysis(Environment environment) {
    this.environment = environment;
  }

  @Override
  public boolean condition() {
    return canSeeTheEnemy();
  }

  private boolean canSeeTheEnemy() {
    return !environment.getActualEnemies().isEmpty();
  }

  private void tryToFindAndKill() {
    assert (!environment.getEnemies().isEmpty());
    ArrayList<TrooperModel> enemies = environment.getActualEnemies();
    ArrayList<TrooperModel> myTroopers = environment.getMyTroopers();
    final TrooperModel enemy = getEnemyClosestToUs(myTroopers, enemies);
    MapCell destination = BattleMap.getMapCellFactory().createMapCell(enemy.getX(), enemy.getY());
    environment.getRouter().setCheckPoint(destination);
  }

  private TrooperModel getEnemyClosestToUs(ArrayList<TrooperModel> myTroopers, ArrayList<TrooperModel> enemies) {
    int minDist = Utils.INFINITY;
    TrooperModel closestEnemy = null;
    for (TrooperModel enemy : enemies) {
      int sumDist = 0;
      for (TrooperModel trooper : myTroopers) {
        sumDist += environment.getBattleMap().getDistance(enemy.getX(), enemy.getY(), trooper.getX(), trooper.getY());
      }
      if (sumDist < minDist) {
        closestEnemy = enemy;
        minDist = sumDist;
      }
    }
    return closestEnemy;
  }

  @Override
  public void doAction() {
    tryToFindAndKill();
  }

  @Override
  public ITactics getTactics() {
    logger.info("CHOOSING TACTICS: THE ENEMY IS VISIBLE");
    return new AttackTactics();
  }
}
