/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatrolTactics implements ITactics {
  private final Environment environment;

  public PatrolTactics(Environment environment) {
    this.environment = environment;
  }
  @Override
  public Action setAction(ITrooperStrategy trooper) {
    return trooper.setActionUnderTactics(this);
  }
}
