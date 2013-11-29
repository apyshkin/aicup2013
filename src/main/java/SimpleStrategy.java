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
        LowerStanceAction action = new LowerStanceAction(currentEnvironment);
        action.act(null, self, move);
        return;
      }

      ShootAction shootAction = new ShootAction(currentEnvironment);
      if (shootAction.hasEnoughAP(self)) {
        Trooper[] troopers = world.getTroopers();
        ArrayList<Trooper> potentialAims = new ArrayList<>();
        for (Trooper trooper : troopers) {
          boolean canShoot = shootAction.canAct(new ShootActionParameters(new TrooperModel(trooper)), self);
          if (canShoot) {
            potentialAims.add(trooper);
          }
        }
        for (Trooper trooper : potentialAims) {
          shootAction.act(new ShootActionParameters(new TrooperModel(trooper)), self, move);
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

    BattleMap battleMap = new BattleMap(world, new CellChecker(world));

    currentEnvironment = new Environment(battleMap, world, game, battleMap.getPathFinder(), 0);
  }
}
