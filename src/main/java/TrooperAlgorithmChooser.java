import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrooperAlgorithmChooser {
  private final static Logger logger = Logger.getLogger(TrooperAlgorithmChooser.class.getName());

  private final TrooperModel trooper;
  private final CellPriorities cellPriorities;
  private final Environment environment;
  private final IActionsGenerator actionsGenerator;

  // for dfs
  private int bestAnswer = -Utils.INFINITY;
  private Action bestAction;
  private IActionParameters bestActionParameters;
  private boolean wasAction;

  public TrooperAlgorithmChooser(Environment environment, TrooperModel trooper, CellPriorities cellPriorities, IActionsGenerator actionsGenerator) {
    this.environment = environment;
    this.trooper = trooper;
    this.cellPriorities = cellPriorities;
    this.actionsGenerator = actionsGenerator;
  }

  Pair<Action, IActionParameters> findBest() {
    try {
      simulateMove(trooper, 0, true);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
    return new Pair<>(bestAction, bestActionParameters);
  }

  private int simulateMove(TrooperModel trooper, int points, boolean firstRun) throws InvalidActionException {
    assert (trooper.getActionPoints() >= 0);
    wasAction = false;

    int maxPoints = -Utils.INFINITY;
    if (trooper.getActionPoints() > 0) {
      TrooperModel trooperCopy = new TrooperModel(trooper);
      ArrayList<Pair<Action, IActionParameters>> listOfActions =
              actionsGenerator.updateActionParametersWithTrooper(trooper);
      for (Pair<Action, IActionParameters> pair : listOfActions) {
        Action currentAction = pair.getKey();
        IActionParameters actionParameters = pair.getValue();
        int possiblePoints = actIfCan(currentAction, actionParameters, points, firstRun);
        maxPoints = Math.max(maxPoints, possiblePoints);
        assert (trooper.equals(trooperCopy));
      }
    }

    if (!wasAction) {
      maxPoints = updateAnswer(trooper, points);
    }
    else if (trooper.getActionPoints() <= 2) {
      maxPoints = Math.max(points + countPotential(trooper), maxPoints);
      if (maxPoints > bestAnswer)
        bestAnswer = maxPoints;
    }

    wasAction = true;
    return maxPoints;
  }

  private int updateAnswer(TrooperModel trooper, int points) {
    int maxPoints = points + countPotential(trooper);
    if (maxPoints > bestAnswer) {
//      logger.log(Level.FINE, "updating answer " + bestAnswer + ", points: " + points + ", potential here: " + (maxPoints - points));
      bestAnswer = maxPoints;
    }
//    logger.log(Level.FINE, "updating maxpoints " + maxPoints + ", points: " + points + ", potential here: " + (maxPoints - points));
    return maxPoints;
  }

  private void updateBest(Action action, IActionParameters actionParameters) {
    logger.log(Level.FINE, "updating best action " + bestAction + " with " + action + " and " + bestAnswer + " points");
    bestAction = action;
    bestActionParameters = actionParameters;
  }

  // returns possible total points we can gain going down this branch
  private int actIfCan(Action action, IActionParameters actionParameters, int points, boolean firstRun) throws InvalidActionException {
    if (action.canAct(actionParameters, trooper)) {
      int pointsForAction = action.actSimulating(actionParameters, trooper);
      int pointsAtThisBranch = simulateMove(trooper, points + pointsForAction, false);
      assert (bestAnswer >= pointsAtThisBranch);

      if (firstRun && bestAnswer == pointsAtThisBranch)
        updateBest(action, actionParameters);
      action.undoActSimulating(actionParameters, trooper);
      actionsGenerator.updateActionParametersWithTrooper(trooper);
      return pointsAtThisBranch;
    }
    return -Utils.INFINITY;
  }

  private int countPotential(TrooperModel trooper) {
//    int teamPlayerScore = countTeamPlayerScore(trooper);

    return cellPriorities.getPriorityAtCell(trooper.getX(), trooper.getY());
  }

//  private int countTeamPlayerScore(TrooperModel trooper) {
//    BattleMap battleMap = environment.getBattleMap();
//    ArrayList<TrooperModel> myTroopers = environment.getMyTroopers();
//    int maxDistance = 0;
//    for (TrooperModel trooper1 : myTroopers)
//      maxDistance = Math.max(maxDistance, battleMap.getDistance(trooper, trooper1));
//    return CellFunctions.GlueTogetherFunction(trooper.getType(), maxDistance);
//  }

}
