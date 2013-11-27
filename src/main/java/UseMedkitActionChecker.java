/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/26/13
 * Time: 6:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class UseMedkitActionChecker extends ActionChecker {
  public UseMedkitActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel healer) {
    UseMedkitActionParameters healParams = (UseMedkitActionParameters) params;
    TrooperModel patient = healParams.getPatient();
    if (countActionCost(healer) > healer.getActionPoints())
      return false;
    if (!checkHasMedkit(healer))
      return false;
    if (!checkCellIsWithinBoundaries(patient.getX(), patient.getY()))
      return false;
    if (!cellChecker.checkCellsAreNeighboursOrTheSame(patient.getX(), patient.getY(), healer.getX(), healer.getY(), UseMedkitActionChecker.this))
      return false;
    if (healer.getPlayerId() != patient.getPlayerId())
      return false;
    if (checkHitPointsAreMax(patient))
      return false;

    return true;
  }

  private boolean checkHasMedkit(TrooperModel healer) {
    return healer.isHoldingMedkit();
  }

  private boolean checkHitPointsAreMax(TrooperModel trooper) {
    return (trooper.getHitpoints() == trooper.getMaximalHitpoints());
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getMedikitUseCost();
  }
}
