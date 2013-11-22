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

  private Environment currentEnvironment = null;
  private BattleHistory battleHistory = new BattleHistory();

  @Override
  public void move(Trooper self, World world, Game game, Move move) {
    try {
      updateEnvironment(world, game);
      updateHistory(currentEnvironment);
      Analyzer analyzer = new Analyzer(currentEnvironment);
      ITactics chosenTactics = analyzer.chooseTactics(battleHistory);
      setAction(self, chosenTactics);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Action setAction(Trooper self, ITactics chosenTactics) throws InvalidTrooperTypeException {
    switch (self.getType()) {
      case COMMANDER:
        return chosenTactics.setAction(new CommanderStrategy(self));
      case SOLDIER:
        return chosenTactics.setAction(new SoldierStrategy(self));
      case FIELD_MEDIC:
        return chosenTactics.setAction(new MedicStrategy(self));
      default:
        throw new InvalidTrooperTypeException(self.getType().toString());
    }
  }

  private void updateHistory(Environment environment) {
    battleHistory.add(environment);
  }

  private void updateEnvironment(World world, Game game) {
    if (currentEnvironment == null) {
      Trooper[] troopers = world.getTroopers();
      ArrayList<Trooper> myTroopers = new ArrayList<>();
      for (Trooper trooper : troopers)
        if (trooper.isTeammate())
          myTroopers.add(trooper);

      currentEnvironment = new Environment(world, game, myTroopers.toArray(troopers));
    }
    else
      currentEnvironment.update(world, game);
  }
}

