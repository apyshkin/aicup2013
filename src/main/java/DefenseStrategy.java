import model.*;

import java.util.ArrayList;
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
  private static BattleState battleState = null;
  private static Router router;
  private static int currentMoveIndex = 0;
  private static Trooper lastTrooper = null;
  private static Analyser analyser;
  private final TimeCounter timeCounter = new TimeCounter();

  @Override
  public void move(Trooper trooper, World world, Game game, Move move) {
    initBattleMap(world, game);
    initBattleState(world);
    logger.info("");
    logger.info("I AM " + new TrooperModel(trooper) + " and I want to move!" + "ITERATION " + world.getMoveIndex());
    try {
      timeCounter.reset();
      initRouter();
      Environment environment = createEnvironment(trooper, world, game, router, world.getMoveIndex());
      updateHistory(environment);
      updateAnalyzer(trooper, environment);
      currentMoveIndex = world.getMoveIndex();

      ITactics chosenTactics = analyser.chooseTactics();

      TrooperModel myTrooper = environment.getMyTeam().getMyTrooper(trooper.getType());
      Pair<AbstractAction, IActionParameters> actionPair = findAction(environment.clone(), myTrooper, chosenTactics); // clone is important
      execute(move, myTrooper, actionPair);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initBattleState(World world) {
    if (battleState == null) {
      long myId = 0;
      ArrayList<TrooperModel> visTroopers = battleMap.getVisibleTroopers();
      int teamSize = 0;
      for (TrooperModel trooper : visTroopers)
        if (trooper.isTeammate()) {
          myId = trooper.getPlayerId();
          ++teamSize;
        }
      battleState = new BattleState(world.getPlayers(), myId, teamSize);
    }
  }

  private void updateAnalyzer(Trooper trooper, Environment environment) {
    World world = environment.getWorld();
    if (nextTrooperMove(trooper, world.getMoveIndex()))
      analyser = new Analyser(environment, battleHistory);
    else
      analyser.setEnvironment(environment);
  }

  private void initRouter() {
    if (router == null) {
      router = new Router(battleMap);
      logger.warning("Router is null");
    }
    if (router.checkPointWasReached()) {
      logger.info("Checkpoint was reached, going to the next one");
      router.nextCheckPoint();
    }
    logger.info("Current checkpoint is " + router.getCheckPoint());
  }

  private void execute(Move move, TrooperModel myTrooper, Pair<AbstractAction, IActionParameters> actionPair) throws InvalidActionException {
    AbstractAction bestAction = actionPair.getKey();
    IActionParameters bestParams = actionPair.getValue();
    logger.info("Chosen action is " + bestAction + " " + bestParams);
    bestAction.act(bestParams, myTrooper, move);
  }

  private void initBattleMap(World world, Game game) {
    if (battleMap == null)
      battleMap = new BattleMap(world, game, new CellChecker(world));
    battleMap.update(world);
  }

  private Pair<AbstractAction, IActionParameters> findAction(Environment environment, TrooperModel trooper, ITactics chosenTactics) throws InvalidTrooperTypeException {
    if (environment.getMyTeam().getLeader() == trooper)
      return chosenTactics.findBestAction(new CommanderStrategy(environment, trooper));
    else
      switch (trooper.getType()) {
        case COMMANDER:
          return chosenTactics.findBestAction(new CommanderStrategy(environment, trooper));
        case SOLDIER:
          return chosenTactics.findBestAction(new SoldierStrategy(environment, trooper));
        case FIELD_MEDIC:
          return chosenTactics.findBestAction(new MedicStrategy(environment, trooper));
        case SNIPER:
          return chosenTactics.findBestAction(new SoldierStrategy(environment, trooper));
        case SCOUT:
          return chosenTactics.findBestAction(new SoldierStrategy(environment, trooper));
        default:
          throw new InvalidTrooperTypeException();
      }
  }

  private void updateHistory(Environment environment) {
    battleHistory.add(environment);
  }

  private boolean nextTrooperMove(Trooper trooper, int newMoveIndex) {
    if (lastTrooper != null && trooper.getType() == lastTrooper.getType() && currentMoveIndex == newMoveIndex) {
      return false;
    }
    lastTrooper = trooper;
    return true;
  }

  private Environment createEnvironment(Trooper trooper, World world, Game game, Router router, int currentTime) {
    ArrayList<TrooperModel> myTroopers = battleMap.getVisibleTroopers();
    for (TrooperModel trooper1 : myTroopers)
      if (trooper1.isTeammate() && trooper1.getType() == trooper.getType())
        return new Environment(trooper1, battleMap, battleState, world, game, router, battleMap.getPathFinder(), currentTime);
    assert false;
    return null;
  }
}
