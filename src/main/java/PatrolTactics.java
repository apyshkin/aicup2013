/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatrolTactics implements ITactics {
  private ActionSequence actionSequence;

  public PatrolTactics() {
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
