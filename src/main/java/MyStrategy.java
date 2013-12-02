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

  static double totalTime = 0;
  static int moveCount = 0;
  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    double millis = System.currentTimeMillis();

    curStrategy.move(self, world, game, move);
    totalTime += System.currentTimeMillis() - millis;
    System.out.println("time for move #" + ++moveCount + " " + (System.currentTimeMillis() - millis) + " : total " + totalTime);
  }
}
