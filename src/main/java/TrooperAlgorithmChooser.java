import model.*;

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
  private Action currentFirstAction = null;
  private IActionParameters currentFirstActionParams = null;

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

  private int simulateMove(TrooperModel trooper, int points, boolean firstRun) throws InvalidActionException {
    assert (trooper.getActionPoints() >= 0);
    final int curPositionPoints = points + countPotential(trooper);
    int maxPoints = curPositionPoints;
    if (trooper.getActionPoints() > 0) {
      TrooperModel trooperCopy = new TrooperModel(trooper);
      ArrayList<Pair<Action, IActionParameters>> listOfActions = actionsGenerator.updateActionParametersWithTrooper(trooper);
      for (Pair<Action, IActionParameters> pair : listOfActions) {
        Action action = pair.getKey();
        IActionParameters actionParameters = pair.getValue();
        int possiblePoints = actIfCan(action, actionParameters, points, firstRun);
        maxPoints = Math.max(maxPoints, possiblePoints);
        assert (trooper.equals(trooperCopy));
      }
    }

    if (maxPoints > bestAnswer)
      updateAnswer(maxPoints, curPositionPoints, firstRun);

    return maxPoints;
  }

  private void updateAnswer(int maxPoints, int curPositionPoints, boolean firstRun) {
    bestAnswer = maxPoints;
    bestAction = currentFirstAction;
    bestActionParameters = currentFirstActionParams;
    if (firstRun && maxPoints == curPositionPoints) {
      logger.info("ending turn is good");
      bestAction = new EndTurnAction(environment);
      bestActionParameters = new EndTurnActionParameters();
    }
  }

  // returns possible total points we can gain going down this branch
  private int actIfCan(Action action, IActionParameters actionParameters, int points, boolean firstRun) throws InvalidActionException {
    if (action.canAct(actionParameters, trooper)) {
      if (firstRun) {
        currentFirstAction = action;
        currentFirstActionParams = actionParameters;
      }
      int pointsForAction = action.actSimulating(actionParameters, trooper);
      int positionPoints = countPositionPoints(trooper);
      int pointsAtThisBranch = simulateMove(trooper, points + pointsForAction + positionPoints, false);
      assert (bestAnswer >= pointsAtThisBranch);
      action.undoActSimulating(actionParameters, trooper);
      actionsGenerator.updateActionParametersWithTrooper(trooper);
      return pointsAtThisBranch;
    }
    return -Utils.INFINITY;
  }

  private int countPositionPoints(TrooperModel trooper) {
    int tx = trooper.getX();
    int ty = trooper.getY();
    int points = 0;

    if (environment.getBattleMap().hasBonus(tx, ty)) {
      Bonus bonus = environment.getBattleMap().getCell(tx, ty).getBonus();
      if (bonus.getType() == BonusType.FIELD_RATION && !trooper.isHoldingFieldRation())
        points = 20;
      else if (bonus.getType() == BonusType.MEDIKIT && !trooper.isHoldingMedkit())
        points = 25 * (trooper.getMaximalHitpoints() / (trooper.getHitpoints() + 10));
      else if (bonus.getType() == BonusType.GRENADE && !trooper.isHoldingGrenade())
        points = 20;
    }
    return points;
  }

  private int countPotential(TrooperModel trooper) {
    int points = 0;
    return points +
            cellPriorities.getPriorityAtCell(trooper.getX(), trooper.getY(),
                    trooper.getStance().ordinal());
  }
}
