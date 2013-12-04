/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPathPotential {
  public void preCount(boolean[][] reachableCells, int coeff);
  public int getPotential(int x, int y, int stance);

  int countNewPathPoints(int currentPathPotential, int newPositionPotential);
}
