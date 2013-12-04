/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class SniperStrategy extends TrooperStrategyAdapter {
  private static final PriorityCoeffPack ATTACK_COEFFICIENTS = new PriorityCoeffPack(6, 4, -6, -4, 1, -1, -2, -200, -3);
  private static final PriorityCoeffPack PATROL_COEFFICIENTS = new PriorityCoeffPack(7, 1, -2, -7, 0, 0, -1, 0, -5);
  private static final PotentialCoeffPack ATTACK_POTENTIALS = new PotentialCoeffPack(1, 1);
  private static final PotentialCoeffPack PATROL_POTENTIALS = new PotentialCoeffPack(1, 1);

  protected SniperStrategy(Environment environment, TrooperModel trooper) {
    super(environment, trooper);
  }

  @Override
  protected IActionsGenerator createActionsGenerator() {
    return new TrooperActionsGenerator(environment);
  }

  @Override
  protected PriorityCoeffPack getAttackPriorityCoeff() {
    return ATTACK_COEFFICIENTS;
  }

  @Override
  protected PriorityCoeffPack getPatrolPriorityCoeff() {
    return PATROL_COEFFICIENTS;
  }

  @Override
  protected PotentialCoeffPack getAttackPotentialCoeff() {
    return ATTACK_POTENTIALS;
  }

  @Override
  protected PotentialCoeffPack getPatrolPotentialCoeff() {
    return PATROL_POTENTIALS;
  }
}
