import model.CellType;
import model.Game;
import model.Trooper;
import model.World;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Environment implements Cloneable {
  private final World world;
  private final Game game;
  private final TrooperModel[] myTroopers;
  private TrooperModel[] visibleTroopers;

  public Environment(World world, Game game, TrooperModel[] myTroopers) {
    this.myTroopers = myTroopers;
    this.world = world;
    this.game = game;
  }

  public Game getGame() {
    return game;
  }

  public TrooperModel[] getAllVisibleTroopers() {
    if (this.visibleTroopers != null)
      return this.visibleTroopers;

    Trooper[] troopers = world.getTroopers();
    ArrayList<TrooperModel> visibleTroopers = new ArrayList<>();
    for (Trooper trooper : troopers)
      if (!trooper.isTeammate())
        visibleTroopers.add(new TrooperModel(trooper));
    this.visibleTroopers = visibleTroopers.toArray(new TrooperModel[0]);
    return this.visibleTroopers;
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

  public boolean enemyIsVisible(TrooperModel self, TrooperModel enemyTrooper) {
    return world.isVisible(self.getShootingRange(), self.getX(), self.getY(), self.getStance(),
            enemyTrooper.getX(), enemyTrooper.getY(), enemyTrooper.getStance());
  }

  public TrooperModel[] getMyTroopers() {
    return myTroopers;
  }

  public World getWorld() {
    return world;
  }

  public Environment clone() {
    World worldClone = Utils.copyOfTheWorld(world);
    TrooperModel[] troopers = new TrooperModel[myTroopers.length];
    System.arraycopy(myTroopers, 0, troopers, 0, myTroopers.length);
    return new Environment(worldClone, game, troopers);
  }
}
