import model.ActionType;
import model.TrooperStance;

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
    logger.info("Expected gained points is " + bestAnswer);
    return new Pair<>(bestAction, bestActionParameters);
  }

  private Action currentFirstAction = null;
  private IActionParameters currentFirstActionParams = null;

  private int simulateMove(TrooperModel trooper, int points, boolean firstRun) throws InvalidActionException {
    assert (trooper.getActionPoints() >= 0);
//    wasAction = false;
//    int maxPoints = -Utils.INFINITY;
    final int curPositionPoints = points + countPotential(trooper);
    int maxPoints = curPositionPoints;
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

    if (maxPoints > bestAnswer) {
      bestAnswer = maxPoints;
      bestAction = currentFirstAction;
      bestActionParameters = currentFirstActionParams;
      if (firstRun && maxPoints == curPositionPoints) {
        logger.info("ending turn is good");
        bestAction = new EndTurnAction(environment);
        bestActionParameters = new EndTurnActionParameters();
      }
    }

//    maxPoints = updateAnswer(trooper, points);

//    if (!wasAction) {
//    maxPoints = updateAnswer(trooper, points);
//    }

//    wasAction = true;
    return maxPoints;
  }

  private int updateAnswer(TrooperModel trooper, int points) {
    int maxPoints = points + countPotential(trooper);
    if (maxPoints > bestAnswer) {
      bestAnswer = maxPoints;
    }
//      logger.log(Level.FINE, "updating answer " + bestAnswer + ", points: " + points + ", potential here: " + (maxPoints - points));
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
      if (firstRun) {
        currentFirstAction = action;
        currentFirstActionParams = actionParameters;
      }
      int pointsForAction = action.actSimulating(actionParameters, trooper);
      int pointsAtThisBranch = simulateMove(trooper, points + pointsForAction, false);
      assert (bestAnswer >= pointsAtThisBranch);

//      if (firstRun && bestAnswer == pointsAtThisBranch)
//        updateBest(action, actionParameters);
      action.undoActSimulating(actionParameters, trooper);
      actionsGenerator.updateActionParametersWithTrooper(trooper);
      return pointsAtThisBranch;
    }
    return -Utils.INFINITY;
  }

  private int countPotential(TrooperModel trooper) {
    int points = 0;
    if (trooper.getStance() == TrooperStance.PRONE)
      points = -5;
    else if (trooper.getStance() == TrooperStance.KNEELING)
      points = -2;
    return points + cellPriorities.getPriorityAtCell(trooper.getX(), trooper.getY());
  }
}
