package strategy.actions;

import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class MoveAction extends Action {

  protected MoveAction(Trooper self, World world, Game game) {
    super(ActionType.MOVE, new MoveActionChecker(game, world), self, world, game);
  }

  @Override
  protected void innerAct(IActionParameters params, Move move) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    move.setX(moveParams.getX());
    move.setY(moveParams.getY());
  }
}
