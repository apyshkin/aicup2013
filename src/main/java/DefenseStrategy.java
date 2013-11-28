import model.*;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 1:48 AM
 * To change this template use File | Settings | File Templates.
 */
public final class DefenseStrategy implements Strategy {
  private final static Logger logger = Logger.getLogger(DefenseStrategy.class.getName());
  private static BattleHistory battleHistory = new BattleHistory();
  private static BattleMap battleMap = null;
  private static int currentTime = 0;
  private static Trooper lastTrooper = null;

  @Override
  public void move(Trooper trooper, World world, Game game, Move move) {
    ++currentTime;
    initBattleMap(world);
    logger.info("I am " + new TrooperModel(trooper) + " and I want to move!");
    try {
      Environment currentEnvironment = createEnvironment(world, game, currentTime);
      if (nextTrooperMove(trooper))
        updateHistory(currentEnvironment);
      Analyzer analyzer = new Analyzer(currentEnvironment);
      ITactics chosenTactics = analyzer.chooseTactics(battleHistory);
      setAction(currentEnvironment.clone(), trooper, chosenTactics, move); // clone is important

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initBattleMap(World world) {
    if (battleMap == null)
      battleMap = new BattleMap(world, new CellChecker(world));
    else
      battleMap.update(world);
  }

  private void setAction(Environment environment, Trooper self, ITactics chosenTactics, Move move) throws InvalidTrooperTypeException {
    TrooperModel selfCopy = environment.getMyTeam().getMyTrooper(self.getType());
    switch (selfCopy.getType()) {
      case COMMANDER:
        chosenTactics.setAction(new CommanderStrategy(environment, selfCopy, move));
        break;
      case SOLDIER:
        chosenTactics.setAction(new SoldierStrategy(environment, selfCopy, move));
        break;
//        chosenTactics.setAction(new SoldierStrategy(environment, selfCopy, move));
      case FIELD_MEDIC:
        chosenTactics.setAction(new MedicStrategy(environment, selfCopy, move));
        break;
      case SNIPER:
        chosenTactics.setAction(new SoldierStrategy(environment, selfCopy, move));
        break;
      default:
        throw new InvalidTrooperTypeException(selfCopy.getType().toString());
    }
  }

  private void updateHistory(Environment environment) {
      battleHistory.add(environment);
  }

  private boolean nextTrooperMove(Trooper self) {
    if (self == lastTrooper) {
      return self.getActionPoints() > lastTrooper.getActionPoints();
    }

    lastTrooper = self;
    return true;
  }


  private Environment createEnvironment(World world, Game game, int currentTime) {
    return new Environment(battleMap, world, game, currentTime);
  }
}
