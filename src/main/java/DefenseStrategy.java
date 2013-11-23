import model.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 1:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class DefenseStrategy implements Strategy {

  private static BattleHistory battleHistory = new BattleHistory();
  private static BattleMap battleMap = null;
  private static int currentTime = 0;

  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    ++currentTime;
    initBattleMap(world);
    try {
      Environment currentEnvironment = createEnvironment(world, game, currentTime);
      updateHistory(currentEnvironment);
      Analyzer analyzer = new Analyzer(currentEnvironment);
      ITactics chosenTactics = analyzer.chooseTactics(battleHistory);
      setAction(currentEnvironment.clone(), self, chosenTactics, move); // clone is important

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initBattleMap(World world) {
    if (battleMap == null)
      battleMap = new BattleMap(world);
  }

  private void setAction(Environment environment, Trooper self, ITactics chosenTactics, Move move) throws InvalidTrooperTypeException {
    TrooperModel selfCopy = new TrooperModel(self);
    switch (selfCopy.getType()) {
      case COMMANDER:
        chosenTactics.setAction(new CommanderStrategy(environment, selfCopy, move));
        break;
      case SOLDIER:
        chosenTactics.setAction(new CommanderStrategy(environment, selfCopy, move));
        break;
//        chosenTactics.setAction(new SoldierStrategy(environment, selfCopy, move));
      case FIELD_MEDIC:
        chosenTactics.setAction(new CommanderStrategy(environment, selfCopy, move));
        break;
//        chosenTactics.setAction(new MedicStrategy(environment, selfCopy, move));
      default:
        throw new InvalidTrooperTypeException(selfCopy.getType().toString());
    }
  }

  private void updateHistory(Environment environment) {
    battleHistory.add(environment);
  }

  private Environment createEnvironment(World world, Game game, int currentTime) {
    Trooper[] troopers = world.getTroopers();
    ArrayList<TrooperModel> myTroopers = new ArrayList<>();
    for (Trooper trooper : troopers)
      if (trooper.isTeammate()) //todo do better
        myTroopers.add(new TrooperModel(trooper));

    return new Environment(battleMap, world, game, myTroopers.toArray(new TrooperModel[0]), currentTime);
  }
}

