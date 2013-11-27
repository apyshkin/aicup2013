import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityCalculator {
  private final Environment environment;
  private final int kPatrol;
  private final int kTeamDensity;
  private final int kDistToHealer;
  private TrooperModel trooper;
  private PatrollingPriority patrollingPriority;
  private TeamDensityPriority teamDensityPriority;
  private DistToHealerPriority distToHealerPriority;

  public PriorityCalculator(Environment environment, TrooperModel trooper, int kPatrol, int kTeamDensity, int kDistToHealer) {
    this.environment = environment;
    this.trooper = trooper;
    this.kPatrol = kPatrol;
    this.kTeamDensity = kTeamDensity;
    this.kDistToHealer = kDistToHealer;
    init();
  }

  private void init() {
    patrollingPriority = new PatrollingPriority(environment, trooper);
    teamDensityPriority = new TeamDensityPriority(environment, trooper);
    distToHealerPriority = new DistToHealerPriority(environment, trooper);
  }

  public CellPriorities getPriorities() {
    World world = environment.getWorld();
    boolean[][] reachableCells = environment.getReachableCells(trooper);

    int[][] priorities = new int[world.getWidth()][world.getHeight()];

    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        if (reachableCells[i][j] && cellHasNoOtherMen(trooper, i, j)) {
          if (environment.getCellChecker().cellIsFree(i, j)) {
            priorities[i][j] += countPatrollingWeight(i, j);
            priorities[i][j] += countTeamDensityWeight(i, j);
            priorities[i][j] += countDistToHealerWeight(i, j);
          }
        }

    return new CellPriorities(environment, priorities);
  }

  private boolean cellHasNoOtherMen(TrooperModel trooper, int x, int y) {
    for (TrooperModel anotherTrooper : environment.getMyTroopers())
      if (trooper != anotherTrooper) {
        if (anotherTrooper.getX() == x && anotherTrooper.getY() == y)
          return false;
      }

    return true;
  }


  private int countPatrollingWeight(int x, int y) {
    return kPatrol * patrollingPriority.getPriority(x, y);
  }

  private int countTeamDensityWeight(int x, int y) {
    return kTeamDensity * teamDensityPriority.getPriority(x, y);
  }

  private int countDistToHealerWeight(int x, int y) {
    return kDistToHealer * distToHealerPriority.getPriority(x, y);
  }
}
