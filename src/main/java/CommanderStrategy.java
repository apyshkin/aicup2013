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

  private static final PriorityCoeffPack ATTACK_PRIORITIES = new PriorityCoeffPack(8, 4, -3, 0, 1, -1, 0, -200, -3);
  private static final PriorityCoeffPack PATROL_PRIORITIES = new PriorityCoeffPack(12, 4, -2, 0, 0, 0, -1, 0, -5);
  private static final PotentialCoeffPack ATTACK_POTENTIALS = new PotentialCoeffPack(1, 1);
  private static final PotentialCoeffPack PATROL_POTENTIALS = new PotentialCoeffPack(1, 3);

  public CommanderStrategy(Environment environment, TrooperModel trooper) {
    super(environment, trooper);
  }

  @Override
  protected IActionsGenerator createActionsGenerator() {
    return new CommanderActionsGenerator(environment);
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

class CommanderActionsGenerator extends TrooperActionsGenerator {
  public CommanderActionsGenerator(Environment environment) {
    super(environment);
  }
}
