import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class MedicStrategy extends TrooperStrategyAdapter {
  private final static Logger logger = Logger.getLogger(MedicStrategy.class.getName());
  private static final PriorityCoeffPack ATTACK_PRIORITIES = new PriorityCoeffPack(1, 3, -9, -18, 1, -1, -1, -150, 0);
  private static final PriorityCoeffPack PATROL_PRIORITIES = new PriorityCoeffPack(5, 3, -5, -9, 0, 0, -1, 0, -10);
  private static final PotentialCoeffPack ATTACK_POTENTIALS = new PotentialCoeffPack(1, 1);
  private static final PotentialCoeffPack PATROL_POTENTIALS = new PotentialCoeffPack(1, 1);

  public MedicStrategy(Environment environment, TrooperModel trooper) {
    super(environment, trooper);
  }

  @Override
  protected IActionsGenerator createActionsGenerator() {
    return new MedicActionsGenerator(environment);
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

