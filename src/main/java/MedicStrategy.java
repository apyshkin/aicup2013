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
  private static final CoefficientPack ATTACK_COEFFICIENTS = new CoefficientPack(1, 4, -8, -8, 1, -1, 0, -100);
  private static final CoefficientPack PATROL_COEFFICIENTS = new CoefficientPack(5, 3, -5, -9, 0, 0, 0, 0);

  public MedicStrategy(Environment environment, TrooperModel trooper) {
    super(environment, trooper);
  }

  @Override
  protected IActionsGenerator createActionsGenerator() {
    return new MedicActionsGenerator(environment);
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

