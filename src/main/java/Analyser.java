import model.Player;
import model.Trooper;
import model.TrooperStance;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Analyser {
  private final static Logger logger = Logger.getLogger(Analyser.class.getName());
  private Environment environment;
  private final BattleHistory battleHistory;
  private AttackTactics attackTactics;
  private PatrolTactics patrolTactics;
  private ArrayList<IAnalysis> analysts;

  public Analyser(Environment environment, BattleHistory battleHistory) {
    this.environment = environment;
    this.battleHistory = battleHistory;
    init();
  }

  private void init() {
    analysts = new ArrayList<>();
    analysts.add(new VisibleEnemyAnalysis(environment, battleHistory));
    analysts.add(new IsUnderAttackAnalysis(environment, battleHistory));
    analysts.add(new CannotFindEnemyAnalysis(environment, battleHistory));
  }

  public ITactics chooseTactics() {
    checkConsistency();

    for (IAnalysis analyst : analysts) {
      if (analyst.condition()) {
        analyst.doAction();
        return analyst.getTactics();
      }
    }
    logger.info("No enemy, just patrolling");
    return getPatrolTactics();
  }

  private void checkConsistency() {
    StringBuilder string = new StringBuilder();
    for (TrooperModel trooper : environment.getEnemies())
      string.append(trooper.toString() + "\n");
    logger.info("Can see the enemies :\n" + string.toString());
    string = new StringBuilder();
    for (TrooperModel trooper : environment.getActualEnemies())
      string.append(trooper.toString() + "\n");
    logger.info("Can see the actual enemies :\n" + string.toString());
    string = new StringBuilder();
    ArrayList<TrooperModel> realEnemies = new ArrayList<>();
    for (Trooper trooper : environment.getWorld().getTroopers())
      if (!trooper.isTeammate()) {
        TrooperModel trooperModel = new TrooperModel(trooper);
        string.append(trooperModel.toString() + "\n");
        realEnemies.add(trooperModel);
      }
    logger.info("Can see the enemies for real:\n" + string.toString());
    assert (realEnemies.size() <= environment.getActualEnemies().size());
  }

  public PatrolTactics getPatrolTactics() {
    if (patrolTactics == null)
      patrolTactics = new PatrolTactics();

    return patrolTactics;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
