import model.World;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityCalculator {
  private final Environment environment;
  private TrooperModel trooper;
  private List<PriorityWeight> priorityWeightList;

  public PriorityCalculator(Environment environment, TrooperModel trooper, List<PriorityWeight> priorityWeightList) {
    this.environment = environment;
    this.trooper = trooper;
    this.priorityWeightList = priorityWeightList;
  }

  public CellPriorities getPriorities() {
    World world = environment.getWorld();
    int[][] priorityWeights = new int[world.getWidth()][world.getHeight()];
    boolean[][] suitableCells = getSuitableCells(trooper);

    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        if (suitableCells[i][j]) {
          int cellWeight = 0;
          for (PriorityWeight weightCounter : priorityWeightList) {
            cellWeight += weightCounter.countCellWeight(i, j);
          }
          priorityWeights[i][j] = cellWeight;
        }

    printAll(suitableCells);
    System.out.println("SUM");
    Utils.printPriorities(world, priorityWeights);
    return new CellPriorities(environment, priorityWeights);
  }

  private void printAll(boolean[][] suitableCells) {
    int[][] weights = new int[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    for (PriorityWeight weightCounter : priorityWeightList) {
    for (int i = 0; i < environment.getWorld().getWidth(); ++i)
      for (int j = 0; j < environment.getWorld().getHeight(); ++j)
        if (suitableCells[i][j]) {
          weights[i][j] = weightCounter.countCellWeight(i, j);
        }
      System.out.println("PRIORITIES " + weightCounter);
      Utils.printPriorities(environment.getWorld(), weights);
    }
  }

  private boolean[][] getSuitableCells(TrooperModel trooper) {
    boolean[][] reachableCells = environment.getReachableCells(trooper);
    System.out.println("REACHABLE");
    Utils.printPriorities(environment.getWorld(), reachableCells);
    for (TrooperModel anotherTrooper : environment.getMyTroopers())
      if (trooper != anotherTrooper)
        reachableCells[anotherTrooper.getX()][anotherTrooper.getY()] = false;
    System.out.println("REACHABLE TROOPERS CONSIDERED");
    Utils.printPriorities(environment.getWorld(), reachableCells);

    return reachableCells;
  }
}
