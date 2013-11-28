import model.Move;

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
  private static final CoefficientPack ATTACK_COEFFICIENTS = new CoefficientPack(0, 1, -3, -3, 0);
  private static final CoefficientPack PATROL_COEFFICIENTS = new CoefficientPack(3, 2, -3, -3, 0);

  public MedicStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
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

