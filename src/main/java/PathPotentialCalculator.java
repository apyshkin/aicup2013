import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class PathPotentialCalculator {
  private final Environment environment;
  private final TrooperModel trooper;
  private final PotentialCoeffPack potentialCoeffPack;
  private final TrooperMemorizer trooperMemorizer;

  public PathPotentialCalculator(Environment environment, TrooperModel trooper, PotentialCoeffPack potentialCoeffPack) {
    this.environment = environment;
    this.trooper = trooper;
    this.potentialCoeffPack = potentialCoeffPack;
    trooperMemorizer = new TrooperMemorizer(trooper);
  }

  private void printAll(boolean[][] suitableCells, IPathPotential[] potentials) {
    int[][] weights = new int[environment.getBattleMap().getWidth()][environment.getBattleMap().getHeight()];
    for (IPathPotential pathPotential : potentials) {
      for (int stance = 2; stance < 3; ++stance) {
        for (int i = 0; i < environment.getWorld().getWidth(); ++i)
          for (int j = 0; j < environment.getWorld().getHeight(); ++j)
            if (suitableCells[i][j]) {
              weights[i][j] = pathPotential.getPotential(i, j, stance);
            }
        System.out.println("POTENTIALS " + pathPotential);
        Utils.printPriorities(environment.getWorld(), weights);
      }
    }
  }

  private boolean[][] getSuitableCells(TrooperModel trooper) {
    boolean[][] reachableCells = environment.getReachableCells(trooper);
    for (TrooperModel anotherTrooper : environment.getMyTroopers())
      if (trooper != anotherTrooper)
        reachableCells[anotherTrooper.getX()][anotherTrooper.getY()] = false;
    return reachableCells;
  }

  public IPathPotential[] getPotentials() {
    ArrayList<IPathPotential> potentialsList = new ArrayList<>();
    double millis = System.currentTimeMillis();
    boolean[][] reachableCells = getSuitableCells(trooper);

    trooperMemorizer.memorize();
    IPathPotential bonusPathPotential = createBonusPathPotential(reachableCells);
    IPathPotential visionPathPotential = createVisionPathPotential(reachableCells);
    potentialsList.add(bonusPathPotential);
    potentialsList.add(visionPathPotential);
    trooperMemorizer.reset();

    System.out.println("time for potentials " + (System.currentTimeMillis() - millis));

    printAll(reachableCells, potentialsList.toArray(new IPathPotential[0]));

    return potentialsList.toArray(new IPathPotential[0]);
  }

  private IPathPotential createVisionPathPotential(boolean[][] reachableCells) {
    IPathPotential visionPathPotential = new VisionPathPotential(environment, trooper);
    visionPathPotential.preCount(reachableCells, potentialCoeffPack.getkVisionPath());
    return visionPathPotential;
  }

  private IPathPotential createBonusPathPotential(boolean[][] reachableCells) {
    IPathPotential bonusPathPotential = new BonusPathPotential(environment, trooper);
    bonusPathPotential.preCount(reachableCells, potentialCoeffPack.getkBonusPath());
    return bonusPathPotential;
  }
}
