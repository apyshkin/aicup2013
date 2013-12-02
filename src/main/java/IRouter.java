/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/25/13
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IRouter {
  public MapCell getCheckPoint();
  public void nextCheckPoint();
  public boolean checkPointWasReached();
  public void setCheckPoint(MapCell checkPoint);
}
