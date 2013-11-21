package strategy.actions;

import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 5:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class LowerStanceAction extends Action {

  public LowerStanceAction(Trooper self, World world, Game game) {
    super(ActionType.LOWER_STANCE, new LowerStanceActionChecker(game, world), self, world, game);
  }

  @Override
  protected void innerAct(IActionParameters params, Move move) {
  }
}

class LowerStanceActionChecker extends ActionChecker {
  public LowerStanceActionChecker(Game game, World world) {
    super(game, world);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, Trooper self) {
    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (self.getStance() == TrooperStance.PRONE)
      return false;

    return true;
  }

  @Override
  public int countActionCost(Trooper self) {
    return game.getStanceChangeCost();
  }
}
