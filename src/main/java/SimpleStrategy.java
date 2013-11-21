import model.*;
import strategy.actions.LowerStanceAction;
import strategy.actions.ShootAction;
import strategy.actions.ShootActionParameters;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 1:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleStrategy implements Strategy {

  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    try {
      if (self.getStance() != TrooperStance.PRONE)
      {
        LowerStanceAction action = new LowerStanceAction(self, world, game);
        action.act(null, move);
      }

      ShootAction shootAction = new ShootAction(self, world, game);
      if (shootAction.cost() <= self.getActionPoints())
      {
        Trooper[] troopers = world.getTroopers();
        for (Trooper trooper : troopers)
        {
          boolean canShoot = shootAction.canAct(new ShootActionParameters(trooper));
          if (canShoot) {
            shootAction.act(new ShootActionParameters(trooper), move);
            return;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
