/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/28/13
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class CoefficientPack {
  private final int kPatrol;
  private final int kTeamDensity;
  private final int kClosenessToHealer;
  private final int kClosenessToLeader;
  private final int kAttack;
  private int kExposure;
  private int kDefense;

  public CoefficientPack(int kPatrol,
                         int kTeamDensity,
                         int kClosenessToHealer,
                         int kClosenessToLeader,
                         int kAttack,
                         int kDefense,
                         int kExposure) {
    this.kPatrol = kPatrol;
    this.kTeamDensity = kTeamDensity;
    this.kClosenessToHealer = kClosenessToHealer;
    this.kClosenessToLeader = kClosenessToLeader;
    this.kAttack = kAttack;
    this.kDefense = kDefense;
    this.kExposure = kExposure;
  }

  public int getkPatrol() {
    return kPatrol;
  }

  public int getkTeamDensity() {
    return kTeamDensity;
  }

  public int getkClosenessToHealer() {
    return kClosenessToHealer;
  }

  public int getkClosenessToLeader() {
    return kClosenessToLeader;
  }

  public int getkAttack() {
    return kAttack;
  }

  public int getkExposure() {
    return kExposure;
  }

  public int getkDefense() {
    return kDefense;
  }
}
