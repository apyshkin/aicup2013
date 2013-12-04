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

  protected TrooperStrategyAdapter(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
  }

  @Override
  public ActionSequence findOptimalActions(AttackTactics tactics) {
    return findOptimalActions(countPriorities(getAttackPriorityCoeff()), countPotentials(getAttackPotentialCoeff()));
  }

  @Override
  public ActionSequence findOptimalActions(PatrolTactics tactics) {
    return findOptimalActions(countPriorities(getPatrolPriorityCoeff()), countPotentials(getPatrolPotentialCoeff()));
  }

  public ActionSequence findOptimalActions(CellPriorities priorities, IPathPotential[] potentials) {
    IActionsGenerator actionsGenerator = createActionsGenerator();
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, potentials, actionsGenerator);
    return algorithmChooser.findBestSequence();
  }

  private IPathPotential[] countPotentials(PotentialCoeffPack potentialCoeffPack) {
    PathPotentialCalculator pathPotentialCalculator = new PathPotentialCalculator(environment, trooper, potentialCoeffPack);
    return pathPotentialCalculator.getPotentials();
  }

  protected CellPriorities countPriorities(PriorityCoeffPack coefficientPack) {
    PriorityWeightsFactory weightsFactory = new PriorityWeightsFactory(environment, trooper);
    List<PriorityWeight> weightList = weightsFactory.createPriorityWeightsList(coefficientPack);
    PriorityCalculator priorityCalculator = new PriorityCalculator(environment, trooper, weightList);
    return priorityCalculator.getPriorities();
  }

  protected abstract IActionsGenerator createActionsGenerator();
  protected abstract PriorityCoeffPack getAttackPriorityCoeff();
  protected abstract PriorityCoeffPack getPatrolPriorityCoeff();
  protected abstract PotentialCoeffPack getAttackPotentialCoeff();
  protected abstract PotentialCoeffPack getPatrolPotentialCoeff();
}
