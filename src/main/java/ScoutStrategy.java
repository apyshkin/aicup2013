/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class ScoutStrategy extends TrooperStrategyAdapter {
  private static final PriorityCoeffPack ATTACK_PRIORITIES = new PriorityCoeffPack(0, 1, -6, -2, 0, -1, -3, -200, -5);
  private static final PriorityCoeffPack PATROL_PRIORITIES = new PriorityCoeffPack(8, 0, 0, 0, 0, 0, -1, 0, -5);
  private static final PotentialCoeffPack ATTACK_POTENTIALS = new PotentialCoeffPack(1, 1);
  private static final PotentialCoeffPack PATROL_POTENTIALS = new PotentialCoeffPack(1, 1);


  protected ScoutStrategy(Environment environment, TrooperModel trooper) {
    super(environment, trooper);
  }

  @Override
  protected IActionsGenerator createActionsGenerator() {
    return new TrooperActionsGenerator(environment);
  }

  @Override
  protected PriorityCoeffPack getAttackPriorityCoeff() {
    return ATTACK_PRIORITIES;
  }

  @Override
  protected PriorityCoeffPack getPatrolPriorityCoeff() {
    return PATROL_PRIORITIES;
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
