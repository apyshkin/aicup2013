/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttackTactics implements ITactics {

  private final Environment environment;

  public AttackTactics(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setAction(ITrooperStrategy trooper) {
    trooper.setActionUnderTactics(this);
  }

  @Override
  public CellPriorities generateCellPriorities() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}

