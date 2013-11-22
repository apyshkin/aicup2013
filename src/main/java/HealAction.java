import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class HealAction extends Action {

  public HealAction(Trooper self, Environment env) {
    super(ActionType.HEAL, new HealActionChecker(env), self, env);
    assert(self.getType() == TrooperType.FIELD_MEDIC);
  }

  @Override
  protected void innerAct(IActionParameters params, Move move) {
    DestinationActionParameters healParams = (DestinationActionParameters)  params;
    move.setX(healParams.getX());
    move.setY(healParams.getY());
  }
}
