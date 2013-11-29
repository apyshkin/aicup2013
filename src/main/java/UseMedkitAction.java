import model.ActionType;
import model.Move;
import model.TrooperType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/26/13
 * Time: 6:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class UseMedkitAction extends Action {
  public UseMedkitAction(Environment environment) {
    super(ActionType.USE_MEDIKIT, new UseMedkitActionChecker(environment), environment);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    UseMedkitActionParameters healParams = (UseMedkitActionParameters) params;
    TrooperModel patient = healParams.getPatient();
    int oldHitPoints = patient.getHitpoints();
    int addingHitPoints = getAdditionalHitPoints(trooper, patient);
    int newHitPoints = oldHitPoints + addingHitPoints;
    patient.setHitpoints(newHitPoints);
    trooper.useMedkit();
    int points = Math.min(newHitPoints, patient.getMaximalHitpoints()) - oldHitPoints;

    return 4 * points;
  }

  private int getAdditionalHitPoints(TrooperModel trooper, TrooperModel patient) {
    return (trooper == patient) ? environment.getGame().getMedikitHealSelfBonusHitpoints()
            : environment.getGame().getMedikitBonusHitpoints();
  }

  @Override
  protected void innerUndoActSimulating(IActionParameters params, TrooperModel healer) {
    UseMedkitActionParameters healParams = (UseMedkitActionParameters) params;
    TrooperModel patient = healParams.getPatient();
    int newHitPoints = patient.getHitpoints();
    int addingHitPoints = getAdditionalHitPoints(healer, patient);
    int oldHitPoints = newHitPoints - addingHitPoints;
    patient.setHitpoints(oldHitPoints);
    healer.obtainMedkit();
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    DestinationActionParameters healParams = (DestinationActionParameters) params;
    move.setX(healParams.getX());
    move.setY(healParams.getY());
  }
}
