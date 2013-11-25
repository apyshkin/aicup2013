import java.util.ArrayList;

public class TrooperAlgorithmChooser {
  public static final int INFINITY = 10000;
  private final TrooperModel trooper;
  private final CellPriorities cellPriorities;
  private final Environment environment;
  private final IActionsGenerator actionsGenerator;

  // for dfs
  private int bestAnswer = -INFINITY;
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

    int maxPoints = -INFINITY;
    if (trooper.getActionPoints() > 0) {
      TrooperModel trooperCopy = new TrooperModel(trooper);
      ArrayList<Pair<Action, IActionParameters>> listOfActions =
              actionsGenerator.updateActionParametersWithTrooper(trooper);
      for (Pair<Action, IActionParameters> pair : listOfActions) {
//        System.out.println("Trooper is here " + trooperCopy);
        Action currentAction = pair.getKey();
        IActionParameters actionParameters = pair.getValue();
//        System.out.println("Trying " + currentAction + " " + actionParameters);
        int possiblePoints = actIfCan(currentAction, actionParameters, points, firstRun);
        maxPoints = Math.max(maxPoints, possiblePoints);
        assert (trooper.equals(trooperCopy));
      }
    }

    if (!wasAction) {
      maxPoints = points + countPotential(trooper);
      if (maxPoints > bestAnswer) {
        bestAnswer = maxPoints;
      }
    }
//      System.out.println("Reached " + points + " points");
    wasAction = true;
    return maxPoints;
  }

  // returns possible total points we can gain going down this branch
  private int actIfCan(Action action, IActionParameters actionParameters, int points, boolean firstRun) throws InvalidActionException {
    if (action.canAct(actionParameters, trooper)) {
      int pointsForAction = action.actSimulating(actionParameters, trooper);
//      System.out.println("best is " + bestAnswer);
      int pointsAtThisBranch = simulateMove(trooper, points + pointsForAction, false);
      assert (bestAnswer >= pointsAtThisBranch);
      if (firstRun && bestAnswer == pointsAtThisBranch)
        updateBest(action, actionParameters);
      action.undoActSimulating(actionParameters, trooper);
      actionsGenerator.updateActionParametersWithTrooper(trooper);
      return pointsAtThisBranch;
    }
    return -INFINITY;
  }

  private void updateBest(Action action, IActionParameters actionParameters) {
    bestAction = action;
    bestActionParameters = actionParameters;
//    System.out.println("Found the best first move " + bestAction + " " + bestActionParameters + " with " + bestAnswer);
  }

  private int countPotential(TrooperModel trooper) {
    BattleMap battleMap = environment.getBattleMap();
    TrooperModel[] myTroopers = environment.getMyTroopers();
    int maxDistance = 0;
    for (TrooperModel trooper1 : myTroopers)
        maxDistance = Math.max(maxDistance, battleMap.getDistance(trooper, trooper1));

    return cellPriorities.getPriorityAtCell(trooper.getX(), trooper.getY())
            + CellFunctions.GlueTogetherFunction(trooper.getType(), maxDistance);
  }

}
