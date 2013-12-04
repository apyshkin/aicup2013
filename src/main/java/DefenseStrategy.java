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
  private static int K = 0;
  private static int L = 0;
  private static final int N = 5;

  @Override
  public void move(Trooper trooper, World world, Game game, Move move) {
    timeCounter.reset();
    initBattleState(world);
    initBattleMap(world, game);
    timeCounter.status("batlemap & state");
    logger.info("");
    logger.info("I AM " + new TrooperModel(trooper) + " and I want to move!" + "ITERATION " + world.getMoveIndex());
    try {
      initRouter();
      Environment environment = createEnvironment(trooper, world, game, router, world.getMoveIndex());
      updateHistory(environment);
      updateAnalyzer(trooper, environment);
      currentMoveIndex = world.getMoveIndex();
      timeCounter.status("initialization");

      ITactics chosenTactics = analyser.chooseTactics();
      timeCounter.status("analysis");

      TrooperModel myTrooper = environment.getMyTeam().getMyTrooper(trooper.getType());
      Pair<AbstractAction, IActionParameters> actionPair = findAction(environment.clone(), myTrooper, chosenTactics); // clone is important

      timeCounter.status("findAction");

      execute(move, myTrooper, actionPair);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initBattleState(World world) {
    if (battleState == null) {
      long myId = 0;
      Trooper[] troopers = world.getTroopers();
      int teamSize = 0;
      for (Trooper trooper : troopers)
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
    if (battleMap == null) {
      battleMap = new BattleMap(game, battleState, new CellChecker(world));
      battleMap.init(world);
    }
    battleMap.update(world);
    if (world.getMoveIndex() % 2 == 0) {
      if (L == N) {
        ++K;
        L = 0;
      }
      if (K == N) {
        logger.info("ALL FILLED");
        return;
      }

      battleMap.getDistanceCounter().fillGradually(K, L++, N);
    }
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
          return chosenTactics.findBestAction(new SniperStrategy(environment, trooper));
        case SCOUT:
          return chosenTactics.findBestAction(new ScoutStrategy(environment, trooper));
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
