import model.Move;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class SoldierStrategy extends TrooperStrategyAdapter {
  private static final CoefficientPack ATTACK_COEFFICIENTS = new CoefficientPack(2, 1, -2, -2);
  private static final CoefficientPack PATROL_COEFFICIENTS = new CoefficientPack(2, 1, -2, -2);

  protected SoldierStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
  }

  @Override
  protected CoefficientPack getAttackCoefficients() {
    return ATTACK_COEFFICIENTS;
  }

  @Override
  protected CoefficientPack getPatrolCoefficients() {
    return PATROL_COEFFICIENTS;
  }
}
