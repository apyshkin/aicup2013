/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
import model.*;

import java.util.Random;

public final class RandomStrategy implements Strategy {
  private final Random random = new Random();

  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    if (self.getActionPoints() < game.getStandingMoveCost()) {
      return;
    }

    move.setAction(ActionType.MOVE);

    if (random.nextBoolean()) {
      move.setDirection(random.nextBoolean() ? Direction.NORTH : Direction.SOUTH);
    } else {
      move.setDirection(random.nextBoolean() ? Direction.WEST : Direction.EAST);
    }
  }
}
