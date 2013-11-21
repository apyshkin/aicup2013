import model.*;

import java.util.Random;

public final class MyStrategy implements Strategy {
  private final Strategy curStrategy = new RandomStrategy();

  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    curStrategy.move(self, world, game, move);
  }
}
