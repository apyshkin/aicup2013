import model.TrooperType;
import java.util.ArrayList;

public class TrooperAlgorithmChooser {
  private TrooperModel trooper;
  private final CellPriorities cellPriorities;
  private final Environment environment;
  private final IActionsGenerator actionsGenerator;


  private TrooperModel trooperCopy;
  private int bestAnswer;
  private SimAction bestAction;
  private IActionParameters bestActionParameters;


  public TrooperAlgorithmChooser(Environment environment, TrooperModel trooper, CellPriorities cellPriorities, IActionsGenerator actionsGenerator) {
    this.environment = environment;
    this.trooper = trooper;
    this.cellPriorities = cellPriorities;
    this.actionsGenerator = actionsGenerator;
  }

  Pair<SimAction, IActionParameters> findBest() {
    try {
      simulateMove(trooper, 0, true);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }

    return new Pair<>(bestAction, bestActionParameters);
  }

  private int simulateMove(TrooperModel trooper, int points, boolean firstRun) throws InvalidActionException {
    assert(trooper.getActionPoints() >= 0);
    boolean wasAction = false;

    int maxPoints = -1;
    if (trooper.getActionPoints() > 0) {
      TrooperModel trooperCopy = new TrooperModel(trooper);
      ArrayList<Pair<SimAction, IActionParameters>> listOfActions =
              actionsGenerator.getActionsWithParameters(trooperCopy);
      for (Pair<SimAction, IActionParameters> pair : listOfActions) {
//        System.out.println("Trooper is here " + trooperCopy);
        SimAction currentAction = pair.getKey();
        IActionParameters actionParameters = pair.getValue();
//        System.out.println("Trying " + currentAction + " " + actionParameters);
        int possiblePoints = actIfCan(trooperCopy, currentAction, actionParameters, points, firstRun);
        if (possiblePoints >= 0) {
          wasAction = true;
          actionsGenerator.getActionsWithParameters(trooper);
          currentAction.reset(actionParameters, trooperCopy);
          assert(trooperCopy.getX() == trooper.getX() && trooper.getY() == trooperCopy.getY() &&
                  trooperCopy.getActionPoints() == trooper.getActionPoints());
        }
        maxPoints = Math.max(maxPoints, possiblePoints);
      }
    }

    if (!wasAction) {
      points += countPotential(trooper.getType(), trooper.getX(), trooper.getY());
//      System.out.println("Reached " + points + " points");
      if (points > bestAnswer) {
        bestAnswer = points;
      }
      return points;
    }
    return maxPoints;
  }

  // returns possible points we can gain going down this branch
  private int actIfCan(TrooperModel trooper, SimAction action, IActionParameters actionParameters, int points, boolean firstRun) throws InvalidActionException {
    if (action.canAct(actionParameters, trooper)) {
      int pointsForAction = action.act(actionParameters, trooper);
      int res = simulateMove(trooper, points + pointsForAction, false);
      if (firstRun && bestAnswer == res) {
        bestAction = action;
        bestActionParameters = actionParameters;
        System.out.println("Found the best first move " + bestAction + " " + bestActionParameters + " with " + bestAnswer);
      }
      return res;
    }
    return -1;
  }

  private int countPotential(TrooperType myTrooperType, int x, int y) {
    return cellPriorities.getPriorityAtCell(x, y)
            + CellFunctions.GlueTogetherFunction(myTrooperType, environment.getMyTroopers());
  }

}
