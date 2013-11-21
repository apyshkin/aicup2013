package strategy.actions;

import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 5:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class RaiseStanceAction extends Action {

  public RaiseStanceAction(Trooper self, World world, Game game) {
    super(ActionType.RAISE_STANCE, new RaiseStanceActionChecker(game, world), self, world, game);
  }

  @Override
  protected void innerAct(IActionParameters params, Move move) {
  }
}

class RaiseStanceActionChecker extends ActionChecker {
  public RaiseStanceActionChecker(Game game, World world) {
    super(game, world);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, Trooper self) {
    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (self.getStance() == TrooperStance.STANDING)
      return false;

    return true;
  }

  @Override
  public int countActionCost(Trooper self) {
    return game.getStanceChangeCost();
  }
}
