import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class SoldierStrategy extends TrooperStrategyAdapter {
  private static final CoefficientPack ATTACK_COEFFICIENTS = new CoefficientPack(4, 2, -8, -4, 1, 1, 0);
  private static final CoefficientPack PATROL_COEFFICIENTS = new CoefficientPack(6, 2, -12, -6, 0, 0, -1);

  protected SoldierStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
  }

  @Override
  protected IActionsGenerator createActionsGenerator() {
    return new TrooperActionsGenerator(environment);
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
