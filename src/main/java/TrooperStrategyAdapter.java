import model.Move;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 1:26 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TrooperStrategyAdapter implements ITrooperStrategy {
  private final static Logger logger = Logger.getLogger(TrooperStrategyAdapter.class.getName());

  protected Environment environment;
  protected TrooperModel trooper;
  protected final Move move;

  protected TrooperStrategyAdapter(Environment environment, TrooperModel trooper, Move move) {
    this.environment = environment;
    this.trooper = trooper;
    this.move = move;
  }

  @Override
  public void setActionUnderTactics(AttackTactics tactics) {
    setAction(countPriorities(getAttackCoefficients()));
  }

  @Override
  public void setActionUnderTactics(PatrolTactics tactics) {
    setAction(countPriorities(getPatrolCoefficients()));
  }

  public void setAction(CellPriorities priorities) {
    CommanderActionsGenerator actionsGenerator = new CommanderActionsGenerator(environment);
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, actionsGenerator);

    Pair<Action, IActionParameters> bestActionWithParams = algorithmChooser.findBest();

    Action bestAction = bestActionWithParams.getKey();
    IActionParameters bestParams = bestActionWithParams.getValue();
    assert (bestAction != null && bestParams != null);
    logger.info("Chosen action is " + bestAction + " " + bestParams);

    try {
      bestAction.act(bestParams, trooper, move);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
  }

  protected CellPriorities countPriorities(CoefficientPack coefficientPack) {
    PriorityWeightsFactory weightsFactory = new PriorityWeightsFactory(trooper, environment);
    List<PriorityWeight> weightList = weightsFactory.createPriorityWeightsList(coefficientPack);
    PriorityCalculator priorityCalculator = new PriorityCalculator(environment, trooper, weightList);
    return priorityCalculator.getPriorities();
  }

  protected abstract CoefficientPack getAttackCoefficients();
  protected abstract CoefficientPack getPatrolCoefficients();
}
