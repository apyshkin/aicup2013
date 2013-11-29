import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class HealAction extends Action {

  public HealAction(Environment env) {
    super(ActionType.HEAL, new HealActionChecker(env), env);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    assert(trooper.getType() == TrooperType.FIELD_MEDIC);
    HealActionParameters healParams = (HealActionParameters) params;
    TrooperModel patient = healParams.getPatient();
    int oldHitPoints = patient.getHitpoints();
    int addingHitPoints = getAdditionalHitPoints(trooper, patient);
    int newHitPoints = oldHitPoints + addingHitPoints;
    patient.setHitpoints(newHitPoints);
    int points = Math.min(newHitPoints, patient.getMaximalHitpoints()) - oldHitPoints;

    return 6 * points;
  }

  private int getAdditionalHitPoints(TrooperModel trooper, TrooperModel patient) {
    return (trooper == patient) ? environment.getGame().getFieldMedicHealSelfBonusHitpoints()
                                : environment.getGame().getFieldMedicHealBonusHitpoints();
  }

  @Override
  protected void innerUndoActSimulating(IActionParameters params, TrooperModel trooper) {
    assert(trooper.getType() == TrooperType.FIELD_MEDIC);
    HealActionParameters healParams = (HealActionParameters) params;
    TrooperModel patient = healParams.getPatient();
    int newHitPoints = patient.getHitpoints();
    int addingHitPoints = getAdditionalHitPoints(trooper, patient);
    int oldHitPoints = newHitPoints - addingHitPoints;
    patient.setHitpoints(oldHitPoints);
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    DestinationActionParameters healParams = (DestinationActionParameters)  params;
    move.setX(healParams.getX());
    move.setY(healParams.getY());
  }
}
