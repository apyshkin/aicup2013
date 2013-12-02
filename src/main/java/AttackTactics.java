/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttackTactics implements ITactics {
  private ActionSequence actionSequence;

  public AttackTactics() {
    actionSequence = null;
  }

  @Override
  public Pair<AbstractAction, IActionParameters> findBestAction(ITrooperStrategy trooperStrategy) {
    actionSequence = initActionSequence(trooperStrategy);
    return actionSequence.next();
  }

  private ActionSequence initActionSequence(ITrooperStrategy trooperStrategy) {
    if (actionSequence == null) {
      actionSequence = trooperStrategy.findOptimalActions(this);
    }
    return actionSequence;
  }
}

