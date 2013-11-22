import model.CellType;
import model.Game;
import model.Trooper;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Environment {
  private World world;
  private Game game;
  private Trooper[] myTroopers;

  public Environment(World world, Game game, Trooper[] troopers) {
    myTroopers = troopers;
    this.world = world;
    this.game = game;
  }

//  public Environment(Environment environment) {
//    World copyOfTheWorld = Utils.copyOfTheWorld(environment.world);
//    Game copyOfTheGame = Utils.copyOfTheGame(environment.game);
//
//    Trooper[] troopersToSave = environment.myTroopers;
//    myTroopers = new Trooper[troopersToSave.length];
//    int i = 0;
//    for (Trooper trooper : troopersToSave)
//      myTroopers[i++] = Utils.copyOfTheTrooper(trooper);
//    world = copyOfTheWorld;
//    game = copyOfTheGame;
//  }

  public Game getGame() {
    return game;
  }

  public Trooper[] getAllVisibleTroopers()
  {
    return world.getTroopers();
  }

  public boolean cellIsWithinBoundaries(int x, int y) {
    if (x < 0 || x >= world.getWidth())
      return false;
    if (y < 0 || y >= world.getHeight())
      return false;
    return true;
  }

  public boolean cellIsFree(int x, int y) {
    return (world.getCells()[x][y] == CellType.FREE);
  }

  public boolean cellsAreNeighbours(int x, int y, int x1, int y1) {
    return Math.abs(x-x1) + Math.abs(y-y1) == 1;
  }

  public boolean enemyIsVisible(Trooper self, Trooper enemyTrooper) {
    return world.isVisible(self.getShootingRange(), self.getX(), self.getY(), self.getStance(),
            enemyTrooper.getX(), enemyTrooper.getY(), enemyTrooper.getStance());
  }

  public Trooper[] getMyTroopers() {
    return myTroopers;
  }

  public World getWorld() {
    return world;
  }
}
