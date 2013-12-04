import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/28/13
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityWeightsFactory {
  private final Environment environment;
  private final TrooperModel trooper;

  public PriorityWeightsFactory(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
  }

  public ArrayList<PriorityWeight> createPriorityWeightsList(PriorityCoeffPack coefficientPack) {
    ArrayList<PriorityWeight> result = new ArrayList<>();
    result.add(new PriorityWeight(new PatrolPriority(environment, trooper), coefficientPack.getkPatrol()));
    result.add(new PriorityWeight(new TeamDensityPriority(environment, trooper), coefficientPack.getkTeamDensity()));
    result.add(new PriorityWeight(new ClosenessToHealerPriority(environment, trooper), coefficientPack.getkClosenessToHealer()));
    result.add(new PriorityWeight(new ClosenessToLeaderPriority(environment, trooper), coefficientPack.getkClosenessToLeader()));
    result.add(new PriorityWeight(new AttackPriority(environment, trooper), coefficientPack.getkAttack()));
    result.add(new PriorityWeight(new DefensePriority(environment, trooper), coefficientPack.getkDefense()));
    result.add(new PriorityWeight(new ExposurePriority(environment, trooper), coefficientPack.getkExposure()));
    result.add(new PriorityWeight(new InvisibilityPriority(environment, trooper), coefficientPack.getkInvisibility()));
    result.add(new PriorityWeight(new AwarenessPriority(environment, trooper), coefficientPack.getkAwareness()));
    return result;
  }
}
