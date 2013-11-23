import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 7:14 AM
 * To change this template use File | Settings | File Templates.
 */
public final class ShootActionParameters extends DestinationActionParameters {

  private TrooperModel enemyTrooper;

  public ShootActionParameters(TrooperModel enemy) {
    super(enemy.getX(), enemy.getY());
    enemyTrooper = enemy;
  }

  public TrooperModel getEnemyTrooper() {
    return enemyTrooper;
  }

  public String toString() {
    return "SHOOT AT " + super.toString();
  }
}
