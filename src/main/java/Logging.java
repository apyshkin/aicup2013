import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/26/13
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Logging {
  private final static Logger logger = Logger.getLogger("");
  private static ConsoleHandler handler = null;

  public static void init(Level level){
    try {
      for (Handler handler : logger.getHandlers())
        logger.removeHandler(handler);
      handler = new ConsoleHandler();
      logger.addHandler(handler);
      handler.setFormatter(new SimpleFormatter());
      handler.setLevel(level);
    } catch (SecurityException e) {
      e.printStackTrace();
    }

  }

  public Logging(Level level) {
    init(level);
    logger.setLevel(level);
    logger.log(Level.INFO, "Initializing Logger");
  }
}
