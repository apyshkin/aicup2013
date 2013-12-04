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
  private final IPathPotential[] pathPotentials;
  private final int pathPotentialsCount;

  // for dfs
  private int bestAnswer = -Utils.INFINITY;
  private ActionSequence actionSequence;
  private ActionSequence bestActionSequence;
  private int bestActionsCount;
  private Pair<AbstractAction, IActionParameters> endTurnActionPair;
  private ArrayList<Pair<AbstractAction, IActionParameters>> listOfActions;
  private EndTurnAction endTurnAction;
  private IActionParameters endTurnActionParameters;

  public TrooperAlgorithmChooser(Environment environment, TrooperModel trooper, CellPriorities cellPriorities, IPathPotential[] pathPotentials, IActionsGenerator actionsGenerator) {
    this.environment = environment;
    this.trooper = trooper;
    this.cellPriorities = cellPriorities;
    this.actionsGenerator = actionsGenerator;
    this.pathPotentials = pathPotentials;
    pathPotentialsCount = pathPotentials.length;
    initEndTurnAction(environment);
  }

  private void initEndTurnAction(Environment environment) {
    endTurnAction = new EndTurnAction(environment);
    endTurnActionParameters = new EndTurnActionParameters();
    endTurnActionPair = new Pair<AbstractAction, IActionParameters>(endTurnAction, endTurnActionParameters);
  }

  private void prepareToDfs() {
    was = new boolean[environment.getBattleMap().getWidth()][environment.getBattleMap().getHeight()];
    was[trooper.getX()][trooper.getY()] = true;
    actionSequence = new ActionSequence(MAX_DEPTH);
    bestActionSequence = new ActionSequence(MAX_DEPTH);
    listOfActions = actionsGenerator.updateActionParametersWithTrooper(trooper);
    oldPotentials = new int[pathPotentialsCount];
  }

  ActionSequence findBestSequence() {
    try {
      timeCounter.reset();
      prepareToDfs();
      simulateMove(trooper, new int[pathPotentialsCount], 0, 0);
      timeCounter.status("time for improved dfs ");
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
    bestActionSequence.trim(bestActionsCount);
    logger.info("Expected gained points is " + bestAnswer + " with sequence " + "\n" + bestActionSequence.toString());
    return bestActionSequence;
  }

  private boolean[][] was;
  private int[] oldPotentials;

  private int simulateMove(TrooperModel trooper, int[] pathPotentialPoints, int points, int depth) throws InvalidActionException {
    assert (trooper.getActionPoints() >= 0);
    final int curPositionPoints = points + countPriority(trooper);
    int maxPoints = curPositionPoints;
    if (trooper.getType() == TrooperType.FIELD_MEDIC || trooper.getActionPoints() > 1) {
      if (trooper.getActionPoints() > 0 && depth < MAX_DEPTH - 1) {
        for (Pair<AbstractAction, IActionParameters> pair : listOfActions) {
          AbstractAction action = pair.getKey();
          IActionParameters actionParameters = pair.getValue();
          actionSequence.set(depth, pair);
          int possiblePoints = actIfCan(action, actionParameters, pathPotentialPoints, points, depth);
          maxPoints = Math.max(maxPoints, possiblePoints);
        }
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
  private int actIfCan(AbstractAction action, IActionParameters actionParameters, int[] pathPotentialPoints, int points, int depth) throws InvalidActionException {
    if (action.canAct(actionParameters, trooper)) {
      int pointsForAction = action.actSimulating(actionParameters, trooper);
      boolean potentialsWereAdded = false;
      if (!was[trooper.getX()][trooper.getY()]) {
        for (int i = 0; i < pathPotentialsCount; ++i) {
          oldPotentials[i] = pathPotentialPoints[i];
          int potential = pathPotentials[i].getPotential(trooper.getX(), trooper.getY(), trooper.getStance().ordinal());
          pathPotentialPoints[i] = pathPotentials[i].countNewPathPoints(potential, pathPotentialPoints[i]);
        }
        was[trooper.getX()][trooper.getY()] = true;
        potentialsWereAdded = true;
      }

      int pointsAtThisBranch = simulateMove(trooper, pathPotentialPoints, points + pointsForAction, depth + 1);
      assert (bestAnswer >= pointsAtThisBranch);
      action.undoActSimulating(actionParameters, trooper);
      was[trooper.getX()][trooper.getY()] = false;
      if (potentialsWereAdded) {
        for (int i = 0; i < pathPotentialsCount; ++i) {
          pathPotentialPoints[i] = oldPotentials[i];
        }
      }
      return pointsAtThisBranch;
    }
    return -Utils.INFINITY;
  }

  private int countPriority(TrooperModel trooper) {
    return cellPriorities.getPriorityAtCell(trooper.getX(), trooper.getY(), trooper.getStance().ordinal());
  }
}
