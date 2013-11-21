package strategy.actions;

import model.Game;
import model.Trooper;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 5:41 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionChecker {
  protected final Game game;
  protected final World world;

  public ActionChecker(Game game, World world) {
    this.game = game;
    this.world = world;
  }

  public abstract boolean checkActionValidity(IActionParameters params, Trooper self);

  public abstract int countActionCost(Trooper self);
}
