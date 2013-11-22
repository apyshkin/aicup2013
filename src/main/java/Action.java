import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 1:29 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Action {
  protected final TrooperModel self;
  protected final Environment environment;

  private ActionType actionType = null;
  protected ActionChecker actionChecker = null;

  protected Action(ActionType actionType, ActionChecker actionChecker, TrooperModel self, Environment env) {
    this.actionType = actionType;
    this.actionChecker = actionChecker;
    this.self = self;
    this.environment = env;
  }

  private void setActionType(Move move) {
    move.setAction(actionType);
  }

  protected boolean isValid(IActionParameters params, TrooperModel self) {
    assert (actionChecker != null);
    return actionChecker.checkActionValidity(params, self);
  }

  protected abstract void innerAct(IActionParameters params, Move move);

  public void act(IActionParameters params, Move move) throws InvalidActionException {
    if (!isValid(params, self))
      throw new InvalidActionException(actionType);
    else {
      setActionType(move);
      innerAct(params, move);
    }
  }

  public int cost() {
    return actionChecker.countActionCost(self);
  }

  public boolean hasEnoughAP() {
    return cost() <= self.getActionPoints();
  }

  public boolean canAct(IActionParameters params) {
    return actionChecker.checkActionValidity(params, self);
  }
}
