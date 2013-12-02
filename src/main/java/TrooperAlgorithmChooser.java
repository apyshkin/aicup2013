import model.*;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TrooperAlgorithmChooser {
  private final static Logger logger = Logger.getLogger(TrooperAlgorithmChooser.class.getName());
  private static final int MAX_DEPTH = 20;
  private final TimeCounter timeCounter = new TimeCounter();
  private final TrooperModel trooper;
  private final CellPriorities cellPriorities;
  private final Environment environment;
  private final IActionsGenerator actionsGenerator;
  private final EndTurnAction endTurnAction;
  private final IActionParameters endTurnActionParameters;
  // for dfs
  private int bestAnswer = -Utils.INFINITY;
  private ActionSequence actionSequence;
  private ActionSequence bestActionSequence;
  private int bestActionsCount;
  private Pair<AbstractAction, IActionParameters> endTurnActionPair;

  public TrooperAlgorithmChooser(Environment environment, TrooperModel trooper, CellPriorities cellPriorities, IActionsGenerator actionsGenerator) {
    this.environment = environment;
    this.trooper = trooper;
    this.cellPriorities = cellPriorities;
    this.actionsGenerator = actionsGenerator;
    endTurnAction = new EndTurnAction(environment);
    endTurnActionParameters = new EndTurnActionParameters();
    endTurnActionPair = new Pair<AbstractAction, IActionParameters>(endTurnAction, endTurnActionParameters);
  }

  ActionSequence findBestSequence() {
    try {
      timeCounter.reset();
      actionSequence = new ActionSequence(MAX_DEPTH);
      bestActionSequence = new ActionSequence(MAX_DEPTH);
      simulateMove(trooper, 0, 0);
      timeCounter.status("time for improved dfs ");
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
    bestActionSequence.trim(bestActionsCount);
    logger.info("Expected gained points is " + bestAnswer + " with sequence " + "\n" + bestActionSequence.toString());
    return bestActionSequence;
  }

  private int simulateMove(TrooperModel trooper, int points, int depth) throws InvalidActionException {
    assert (trooper.getActionPoints() >= 0);
    final int curPositionPoints = points + countPotential(trooper);
    int maxPoints = curPositionPoints;
    if (trooper.getActionPoints() > 0 && depth < MAX_DEPTH - 1) {
      ArrayList<Pair<AbstractAction, IActionParameters>> listOfActions = actionsGenerator.updateActionParametersWithTrooper(trooper);
      for (Pair<AbstractAction, IActionParameters> pair : listOfActions) {
        AbstractAction action = pair.getKey();
        IActionParameters actionParameters = pair.getValue();
        actionSequence.set(depth, pair);
        int possiblePoints = actIfCan(action, actionParameters, points, depth);
        maxPoints = Math.max(maxPoints, possiblePoints);
      }
    }

    if (maxPoints > bestAnswer || isAFasterWay(depth, curPositionPoints, maxPoints))
      updateAnswer(maxPoints, depth);

    return maxPoints;
  }

  private boolean isAFasterWay(int depth, int curPositionPoints, int maxPoints) {
    return (maxPoints == bestAnswer && curPositionPoints == bestAnswer && (depth + 1) < bestActionsCount);
  }

  private void updateAnswer(int maxPoints, int depth) {
    bestAnswer = maxPoints;
    bestActionsCount = depth + 1;
    for (int i = 0; i < depth; ++i) {
      bestActionSequence.set(i, actionSequence.get(i));
    }
    bestActionSequence.set(depth, endTurnActionPair);
  }

  // returns possible total points we can gain going down this branch
  private int actIfCan(AbstractAction action, IActionParameters actionParameters, int points, int depth) throws InvalidActionException {
    if (action.canAct(actionParameters, trooper)) {
      int pointsForAction = action.actSimulating(actionParameters, trooper);
      int positionPoints = countPositionPoints(trooper);
      int pointsAtThisBranch = simulateMove(trooper, points + pointsForAction + positionPoints, depth + 1);
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
      Bonus bonus = environment.getBattleMap().getBonus(tx, ty);
      if (bonus.getType() == BonusType.FIELD_RATION && !trooper.isHoldingFieldRation())
        points += 10;
      else if (bonus.getType() == BonusType.MEDIKIT && !trooper.isHoldingMedkit())
        points += 30 * (100 / (trooper.getHitpoints() + 10));
      else if (bonus.getType() == BonusType.GRENADE && !trooper.isHoldingGrenade())
        points += 25;
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
