import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/1/13
 * Time: 9:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class EnemyLocator {
  private final static Logger logger = Logger.getLogger(EnemyLocator.class.getName());
  private final HashMap<TrooperModel, Integer> enemies;

  public EnemyLocator() {
    enemies = new HashMap<>();
  }

  public void updateEnemies(ArrayList<TrooperModel> enemyList, int currentTime) {
    for (TrooperModel enemy : enemyList)
      enemies.put(enemy, currentTime);
  }

  public ArrayList<TrooperModel> getEnemies() {
    return new ArrayList<>(enemies.keySet());
  }

  public ArrayList<TrooperModel> getLastSeenEnemies() {
    ArrayList<TrooperModel> result = new ArrayList<>();
    int maxTime = 0;
    for (TrooperModel enemy : enemies.keySet()) {
      int lastTimeSeen = enemies.get(enemy);
      if (maxTime <= lastTimeSeen) {
        if (maxTime < lastTimeSeen)
          result.clear();
        maxTime = enemies.get(enemy);
        result.add(enemy);
      }
    }
//    if (!result.isEmpty())
//      logger.info("last seen enemy at " + enemies.get(result.get(0)));
//    else
//      logger.info("have not seen enemy at " + " all");

    return result;
  }
}
