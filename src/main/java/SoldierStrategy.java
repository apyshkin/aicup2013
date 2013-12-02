/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class SoldierStrategy extends TrooperStrategyAdapter {
  private static final CoefficientPack ATTACK_COEFFICIENTS = new CoefficientPack(4, 4, -9, -4, 1, -1, 0, -100);
  private static final CoefficientPack PATROL_COEFFICIENTS = new CoefficientPack(6, 2, -4, -9, 0, 0, 0, 0);

  protected SoldierStrategy(Environment environment, TrooperModel trooper) {
    super(environment, trooper);
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
