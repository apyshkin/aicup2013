import model.Move;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommanderStrategy extends TrooperStrategyAdapter {
  private final static Logger logger = Logger.getLogger(CommanderStrategy.class.getName());

  private static final CoefficientPack ATTACK_COEFFICIENTS = new CoefficientPack(4, 2, -2, 0, 1);
  private static final CoefficientPack PATROL_COEFFICIENTS = new CoefficientPack(6, 2, -4, 0, 0);

  public CommanderStrategy(Environment environment, TrooperModel trooper, Move move) {
    super(environment, trooper, move);
  }

  @Override
  protected IActionsGenerator createActionsGenerator() {
    return new CommanderActionsGenerator(environment);
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

class CommanderActionsGenerator extends TrooperActionsGenerator {
  public CommanderActionsGenerator(Environment environment) {
    super(environment);
  }
}
