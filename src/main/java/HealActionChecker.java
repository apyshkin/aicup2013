import model.TrooperType;

public class HealActionChecker extends ActionChecker {
  public HealActionChecker(Environment env) {
    super(env);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel healer) {
    HealActionParameters healParams = (HealActionParameters) params;
    TrooperModel patient = healParams.getPatient();
    if (healer.getType() != TrooperType.FIELD_MEDIC)
      return false;
    if (countActionCost(healer) > healer.getActionPoints())
      return false;
    if (!checkCellIsWithinBoundaries(patient.getX(), patient.getY()))
      return false;
    if (!cellChecker.checkCellsAreNeighboursOrTheSame(patient.getX(), patient.getY(), healer.getX(), healer.getY(), HealActionChecker.this))
      return false;
    if (healer.getPlayerId() != patient.getPlayerId())
      return false;
    if (checkHitPointsAreMax(patient))
      return false;

    return true;
  }

  private boolean checkHitPointsAreMax(TrooperModel trooper) {
    return (trooper.getHitpoints() == trooper.getMaximalHitpoints());
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getFieldMedicHealCost();
  }
}
