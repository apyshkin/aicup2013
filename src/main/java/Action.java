import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:52 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Action {
  protected final Environment environment;
  private final ActionChecker actionChecker;
  private final ActionType actionType;

  protected Action(ActionType actionType, ActionChecker actionChecker, Environment env) {
    this.actionType = actionType;
    this.actionChecker = actionChecker;
    this.environment = env;
  }

  private void setActionType(Move move) {
    move.setAction(actionType);
  }

  protected boolean isValid(IActionParameters params, TrooperModel trooper) {
    assert (actionChecker != null);
    return actionChecker.checkActionValidity(params, trooper);
  }

  protected abstract int innerActSimulating(IActionParameters params, TrooperModel trooper);

  public int actSimulating(IActionParameters params, TrooperModel trooper) throws InvalidActionException {
    if (!isValid(params, trooper))
      throw new InvalidActionException(actionType);
    else {
      trooper.setActionPoints(trooper.getActionPoints() - cost(trooper));
      int actionPoints = innerActSimulating(params, trooper);
      assert(actionPoints >= 0);
      return actionPoints;
    }
  }

  protected abstract void innerUndoActSimulating(IActionParameters actionParameters, TrooperModel trooper);

  public void undoActSimulating(IActionParameters actionParameters, TrooperModel trooper) {
    trooper.setActionPoints(trooper.getActionPoints() + cost(trooper));
    innerUndoActSimulating(actionParameters, trooper);
  }

  public int cost(TrooperModel trooper) {
    return actionChecker.countActionCost(trooper);
  }

  public boolean hasEnoughAP(TrooperModel trooper) {
    return cost(trooper) <= trooper.getActionPoints();
  }

  public boolean canAct(IActionParameters params, TrooperModel trooper) {
    return actionChecker.checkActionValidity(params, trooper);
  }

  public void act(IActionParameters params, TrooperModel trooper, Move move) throws InvalidActionException {
    if (!isValid(params, trooper))
      throw new InvalidActionException(actionType);
    else {
      setActionType(move);
      innerAct(params, trooper, move);
    }
  }

  protected abstract void innerAct(IActionParameters params, TrooperModel trooper, Move move);
}
