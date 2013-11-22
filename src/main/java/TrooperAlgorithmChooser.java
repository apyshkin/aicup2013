import javafx.util.Pair;

import java.util.ArrayList;

public class TrooperAlgorithmChooser {
  private TrooperModel trooper;
  private final CellPriorities cellPriorities;
  private final Environment environment;
  private final IActionsGenerator actionsGenerator;


  private TrooperModel trooperCopy;
  private int bestAnswer;
  private SimulatedAction bestAction;
  private IActionParameters bestActionParameters;


  public TrooperAlgorithmChooser(Environment environment, TrooperModel trooper, CellPriorities cellPriorities, IActionsGenerator actionsGenerator) {
    this.environment = environment;
    this.trooper = trooper;
    this.cellPriorities = cellPriorities;
    this.actionsGenerator = actionsGenerator;
  }

  Pair<SimulatedAction, IActionParameters> findBest() {
    try {
//      trooperCopy = new TrooperModel(trooper);
      simulateMove(trooper, 0, true);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }

    return new Pair<>(bestAction, bestActionParameters);
  }

  private int simulateMove(TrooperModel trooper, int points, boolean firstRun) throws InvalidActionException {
    assert(trooper.getActionPoints() >= 0);
    boolean wasAction = false;
    System.out.println(trooper + " " + trooper.getActionPoints());

    TrooperModel trooperCopy = new TrooperModel(trooper);
    int maxPoints = -1;
    ArrayList<Pair<SimulatedAction, IActionParameters>> listOfActions =
            actionsGenerator.getActionsWithParameters(trooperCopy);
    for (Pair<SimulatedAction, IActionParameters> pair : listOfActions) {
      SimulatedAction currentAction = pair.getKey();
      IActionParameters actionParameters = pair.getValue();
      System.out.print(trooper + " " + trooper.getActionPoints() + " ");
      System.out.println(actionParameters);
      int possiblePoints = actIfCan(trooperCopy, currentAction, actionParameters, points, firstRun);
      if (possiblePoints >= 0) {
        wasAction = true;
        trooperCopy = new TrooperModel(trooper);
      }
      maxPoints = Math.max(maxPoints, possiblePoints);
    }

    if (!wasAction) {
      points += countPotential(trooper.getX(), trooper.getY());
      if (points > bestAnswer) {
        bestAnswer = points;
        System.out.println("position " + trooper.getX() + " " + trooper.getY() + " point " + points);
      }
      return points;
    }
    return maxPoints;
  }

  private void copyTroopers(TrooperModel trooper, TrooperModel trooperCopy) {
    trooperCopy.setActionPoints(trooper.getActionPoints());
    trooperCopy.setStance(trooper.getStance());
    trooperCopy.setX(trooper.getX());
    trooperCopy.setY(trooper.getY());
  }

  // returns possible points we can gain going down this branch
  private int actIfCan(TrooperModel trooper, SimulatedAction action, IActionParameters actionParameters, int points, boolean firstRun) throws InvalidActionException {
    if (action.canAct(actionParameters, trooper)) {
      action.act(actionParameters, trooper);
      int res = simulateMove(trooper, points, false);
      if (firstRun && bestAnswer == res) {
        bestAction = action;
        bestActionParameters = actionParameters;
      }
      return res;
    }
    return -1;
  }


  private int countPotential(int x, int y) {
    return cellPriorities.getPriorityAtCell(x, y);
  }

}
