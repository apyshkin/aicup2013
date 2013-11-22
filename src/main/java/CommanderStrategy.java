import model.*;

import java.util.ArrayList;

import static java.lang.StrictMath.hypot;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommanderStrategy extends TrooperStrategyAdapter {

  public CommanderStrategy(Trooper self) {
    super(self);
  }

  @Override
  public Action setActionUnderTactics(AttackTactics attackTactics) {
    Trooper clone = Utils.copyOfTheTrooper(self);

  }

  @Override
  public Action setActionUnderTactics(PatrolTactics patrolTactics) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

}

class CellPriorities {
  private final Environment environment;
  private int[][] cells;

  public CellPriorities(Environment environment) {
    this.environment = environment;
    World world = environment.getWorld();
    cells = new int[world.getWidth()][world.getHeight()];
  }

  public int getPriorityAtCell(int x, int y) {
    return cells[x][y];
  }
}

class TrooperAlgorithmChooser {

  private final Trooper trooper;
  private final CellPriorities cellPriorities;
  private final Environment environment;

  public TrooperAlgorithmChooser(Trooper trooper, CellPriorities cellPriorities, Environment environment) {
    this.trooper = trooper;
    this.cellPriorities = cellPriorities;
    this.environment = environment;
  }

  ArrayList<Action> findBest() {
    ArrayList<Action> result = new ArrayList<>();
    int answer = 10000;
    TrooperModel trooperModel = new TrooperModel(trooper);
    answer = simulateMove(trooperModel, result, answer);

    return result;
  }

  private int simulateMove(TrooperModel trooper, ArrayList<Action> result, int answer) {
    assert (trooper.getActionPoints() >= 0);

    Action shootAction = new ShootAction(trooper, environment);


    return answer;  //To change body of created methods use File | Settings | File Templates.
  }

//  private int simulateMove(int x, int y, int actionPoints, int stance, int[] actions,
//                           int[] actionsAimX, int[] actionsAimY, int actionCount, int answer) {
//    if (actionPoints < 0)
//      assert(false);
//    else if (actionPoints == 0)
//      return answer;
//
//    final int stanceChangeCost = environment.getGame().getStanceChangeCost();
//    int moveCost = countMoveCost(stance);
//    int shootCost = countShootCost(stance);
//    tryChangeStance(x, y, actionPoints, stance, actions, actionsAimX, actionsAimY, actionCount, answer);
//  }
//
//  private void tryChangeStance(int x, int y, int actionPoints, int stance, int[] actions, int[] actionsAimX, int[] actionsAimY, int actionCount, int answer) {
//    actions[answer++] = 0;
//    if ()
//    simulateMove(x, y, act)
//  }
//
//  private final String[] actionMap = new String[] {
//          "high",
//          "low" ,
//          "move",
//          "shoot",
//  };

//  private int countShootCost(int stance) {
//    assert(false);
//    return 0;  //To change body of created methods use File | Settings | File Templates.
//  }
//
//  private int countMoveCost(int stance) {
//    switch (stance) {
//      case 0: return environment.getGame().getProneMoveCost();
//      case 1: return environment.getGame().getKneelingMoveCost();
//      case 2: return environment.getGame().getStandingMoveCost();
//      default: assert(false);
//    }
//    return -1;
//  }
//}

}
