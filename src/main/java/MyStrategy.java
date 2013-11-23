import model.*;

public final class MyStrategy implements Strategy {
 private final Strategy curStrategy = new DefenseStrategy();
  //private final Strategy curStrategy = new DefenseStrategy();

  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    curStrategy.move(self, world, game, move);
  }
}
