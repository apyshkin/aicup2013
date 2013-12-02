/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPotential {
  public void preCount(boolean[][] reachableCells);
  public int getPotential(int x, int y, int stance);
}
