import model.Bonus;
import model.BonusType;
import model.TrooperStance;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class BonusPathPotential implements IPathPotential {
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][] potentials;
  private int coeff;

  public BonusPathPotential(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    init();
  }

  private void init() {
  }

  @Override
  public void preCount(boolean[][] reachableCells, int coeff) {
    this.coeff = coeff;
    final BattleMap battleMap = environment.getBattleMap();
    TrooperMemorizer memorizer = new TrooperMemorizer(trooper);
    memorizer.memorize();
    potentials = new int[battleMap.getWidth()][battleMap.getHeight()];
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j)
        if (reachableCells[i][j]) {
          trooper.move(i, j, TrooperStance.STANDING);
          potentials[i][j] = coeff * countPositionPoints(trooper);
        }
    memorizer.reset();
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
        points += 30 * (150 / (trooper.getHitpoints() + 10));
      else if (bonus.getType() == BonusType.GRENADE && !trooper.isHoldingGrenade())
        points += 25;
    }
    return points;
  }

  @Override
  public int getPotential(int x, int y, int stance) {
    return potentials[x][y];
  }

  @Override
  public int countNewPathPoints(int currentPathPotential, int newPositionPotential) {
    return currentPathPotential + newPositionPotential;
  }

  @Override
  public String toString() {
    return "Potential for " + this.getClass().getSimpleName() + " : coefficient = " + coeff;
  }
}
