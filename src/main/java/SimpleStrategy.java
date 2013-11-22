import model.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 1:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleStrategy implements Strategy {

  private Environment currentEnvironment = null;
  private BattleHistory history = new BattleHistory();

  @Override
  public void move(Trooper selfTrooper, World world, Game game, Move move) {
    try {
      TrooperModel self = new TrooperModel(selfTrooper);
      updateEnvironment(world, game);
      updateHistory(currentEnvironment);

      if (self.getStance() != TrooperStance.KNEELING) {
        LowerStanceAction action = new LowerStanceAction(self, currentEnvironment);
        action.act(null, move);
        return;
      }

      ShootAction shootAction = new ShootAction(self, currentEnvironment);
      if (shootAction.hasEnoughAP()) {
        Trooper[] troopers = world.getTroopers();
        ArrayList<Trooper> potentialAims = new ArrayList<>();
        for (Trooper trooper : troopers) {
          boolean canShoot = shootAction.canAct(new ShootActionParameters(new TrooperModel(trooper)));
          if (canShoot) {
            potentialAims.add(trooper);
          }
        }
        for (Trooper trooper : potentialAims) {
          shootAction.act(new ShootActionParameters(new TrooperModel(trooper)), move);
          return;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateHistory(Environment environment) {
    history.add(environment);
  }

  private void updateEnvironment(World world, Game game) {
    Trooper[] troopers = world.getTroopers();
    ArrayList<TrooperModel> myTroopers = new ArrayList<>();
    for (Trooper trooper : troopers)
      if (trooper.isTeammate())
        myTroopers.add(new TrooperModel(trooper));

    currentEnvironment = new Environment(world, game, myTroopers.toArray(new TrooperModel[0]));
  }
}
