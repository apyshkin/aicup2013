import model.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class MyStrategy implements Strategy {
  private final static Logger logger = Logger.getLogger(MyStrategy.class.getName());
  private final Strategy curStrategy = new DefenseStrategy();

  private static final Level LOGGING_LEVEL = Level.INFO;

  static {
    new Logging(LOGGING_LEVEL);
  }

  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    curStrategy.move(self, world, game, move);
  }
}
