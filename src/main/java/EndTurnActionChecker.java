import java.util.logging.Logger;

public class EndTurnActionChecker extends ActionChecker {
  private final static Logger logger = Logger.getLogger(EndTurnActionChecker.class.getName());

  public EndTurnActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
//    return (trooper.getActionPoints() < 8);
    if (trooper.getActionPoints() >= 4)
      logger.warning("trooper " + trooper + " wants to end his turn");
    return true;
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return 0;
  }
}
