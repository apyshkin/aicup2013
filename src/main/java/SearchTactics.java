/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchTactics implements ITactics {
  private Environment environment;

  public SearchTactics(Environment environment) {
    this.environment = environment;
  }
  @Override
  public Pair<AbstractAction, IActionParameters> findBestAction(ITrooperStrategy trooperStrategy) {
    AbstractAction action = new CommanderAction(environment);
    return new Pair<AbstractAction, IActionParameters>(action, null);
  }
}
