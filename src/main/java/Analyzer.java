import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Analyzer {

  private final Environment currentEnvironment;

  public Analyzer(Environment environment) {
    currentEnvironment = environment;
  }

  public ITactics chooseTactics(BattleHistory battleHistory) {
    if (wasAttackedSinceLastMove(battleHistory)) {
      return new AttackTactics();
    }
    else
      return new PatrolTactics();
  }

  private boolean wasAttackedSinceLastMove(BattleHistory battleHistory) {
    if (battleHistory.size() < 2)
      return false;

    Environment last = battleHistory.getLast();
    Environment oneBeforeLast = battleHistory.get(battleHistory.size() - 2);
    int summaryHP = 0;
    for (Trooper myTrooper : last.getMyTroopers())
      summaryHP += myTrooper.getHitpoints();

    int summaryHPBeforeLastMove = 0;
    for (Trooper myTrooper : oneBeforeLast.getMyTroopers())
      summaryHPBeforeLastMove += myTrooper.getHitpoints();

    if (summaryHPBeforeLastMove < summaryHP)
      return true;
    else
      return false;
  }

}
