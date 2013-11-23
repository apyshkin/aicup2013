import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:52 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SimAction {
  protected final Environment environment;

  private ActionType actionType = null;
  protected ActionChecker actionChecker = null;

  protected SimAction(ActionType actionType, ActionChecker actionChecker, Environment env) {
    this.actionType = actionType;
    this.actionChecker = actionChecker;
    this.environment = env;
  }

  private void setActionType(Move move) {
    move.setAction(actionType);
  }

  protected boolean isValid(IActionParameters params, TrooperModel self) {
    assert (actionChecker != null);
    return actionChecker.checkActionValidity(params, self);
  }

  protected abstract int innerAct(IActionParameters params, TrooperModel self);

  public int act(IActionParameters params, TrooperModel self) throws InvalidActionException {
    if (!isValid(params, self))
      throw new InvalidActionException(actionType);
    else {
      self.setActionPoints(self.getActionPoints() - cost(self));
      return innerAct(params, self);
    }
  }

  public int cost(TrooperModel self) {
    return actionChecker.countActionCost(self);
  }

  public boolean hasEnoughAP(TrooperModel self) {
    return cost(self) <= self.getActionPoints();
  }

  public boolean canAct(IActionParameters params, TrooperModel self) {
    return actionChecker.checkActionValidity(params, self);
  }

  public void actReal(IActionParameters params, TrooperModel self, Move move) throws InvalidActionException {
    if (!isValid(params, self))
      throw new InvalidActionException(actionType);
    else {
      setActionType(move);
      innerActForReal(params, self, move);
    }
  }

  protected abstract void innerActForReal(IActionParameters params, TrooperModel self, Move move);

  public abstract void reset(IActionParameters actionParameters, TrooperModel trooper);
}
