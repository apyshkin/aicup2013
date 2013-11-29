import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/26/13
 * Time: 6:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class EatFieldRationAction extends Action {
  public EatFieldRationAction(Environment environment) {
    super(ActionType.EAT_FIELD_RATION, new EatFieldRationActionChecker(environment), environment);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    int oldAP = trooper.getActionPoints();
    int addingAP = getAdditionalAP();
    int newAP = oldAP + addingAP;
    trooper.setActionPoints(newAP);
    trooper.eatFieldRation();
    int points = Math.min(newAP, trooper.getInitialActionPoints()) - oldAP;

    return points << 1;
  }

  private int getAdditionalAP() {
    return environment.getGame().getFieldRationBonusActionPoints();
  }

  @Override
  protected void innerUndoActSimulating(IActionParameters params, TrooperModel trooper) {
    int newAP = trooper.getActionPoints();
    int addingAP = getAdditionalAP();
    int oldAP = newAP - addingAP;
    trooper.setActionPoints(oldAP);
    trooper.obtainFieldRation();
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    move.setX(((DestinationActionParameters) params).getX());
    move.setY(((DestinationActionParameters) params).getY());
  }
}
