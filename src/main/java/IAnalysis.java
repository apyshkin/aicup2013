/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IAnalysis {
  public boolean condition();

  public void doAction();

  public ITactics getTactics();
}
