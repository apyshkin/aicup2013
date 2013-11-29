import model.TrooperStance;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExposurePriority implements IPriority {
  private final Environment environment;
  private final TrooperModel trooper;

  public ExposurePriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    assert (trooper.getStance() == Utils.getStance(stance));
    return environment.getBattleMap().getExposure(trooper, x, y);
  }
}
