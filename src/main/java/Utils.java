import model.Direction;
import model.Game;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
  public static final int INFINITY = 10000;

  public static World copyOfTheWorld(World world) {
    return new World(world.getMoveIndex(), world.getWidth(), world.getHeight(), world.getPlayers(),
            world.getTroopers(), world.getBonuses(), world.getCells(), world.getCellVisibilities());
  }

  public static Game copyOfTheGame(Game game) {
    return new Game(game.getMoveCount(), game.getLastPlayerEliminationScore(), game.getPlayerEliminationScore(),
            game.getTrooperEliminationScore(), game.getTrooperDamageScoreFactor(), game.getStanceChangeCost(), game.getStandingMoveCost(),
            game.getKneelingMoveCost(), game.getProneMoveCost(), game.getCommanderAuraBonusActionPoints(), game.getCommanderAuraRange(),
            game.getCommanderRequestEnemyDispositionCost(), game.getCommanderRequestEnemyDispositionMaxOffset(),
            game.getFieldMedicHealCost(), game.getFieldMedicHealBonusHitpoints(), game.getFieldMedicHealSelfBonusHitpoints(), game.getSniperStandingStealthBonus(),
            game.getSniperKneelingStealthBonus(), game.getSniperProneStealthBonus(), game.getSniperStandingShootingRangeBonus(), game.getSniperKneelingShootingRangeBonus(), game.getSniperProneShootingRangeBonus(),
            game.getScoutStealthBonusNegation(), game.getGrenadeThrowCost(), game.getGrenadeThrowRange(), game.getGrenadeDirectDamage(),
            game.getGrenadeCollateralDamage(), game.getMedikitUseCost(), game.getMedikitBonusHitpoints(), game.getMedikitHealSelfBonusHitpoints(), game.getFieldRationEatCost(), game.getFieldRationBonusActionPoints());
  }

  public static TrooperModel copyOfTheTrooper(TrooperModel trooper) {
    return new TrooperModel(trooper.getPlayerId(), trooper.getX(), trooper.getY(), trooper.getPlayerId(), trooper.getTeammateIndex(),
            trooper.isTeammate(), trooper.getType(), trooper.getStance(), trooper.getHitpoints(), trooper.getMaximalHitpoints(),
            trooper.getActionPoints(), trooper.getInitialActionPoints(), trooper.getVisionRange(), trooper.getShootingRange(),
            trooper.getShootCost(), trooper.getStandingDamage(), trooper.getKneelingDamage(), trooper.getProneDamage(),
            trooper.isHoldingGrenade(), trooper.isHoldingMedkit(), trooper.isHoldingFieldRation());
  }

  public static Direction getOppositeDirection(Direction direction) {
    switch (direction) {
      case NORTH : return Direction.SOUTH;
      case SOUTH : return Direction.NORTH;
      case WEST : return Direction.EAST;
      case EAST : return Direction.WEST;
      default :
        assert(false);
    }
    return null;
  }

  public  static void printPriorities(World world, int[][] priorities) {
    for (int i = 0; i < world.getHeight(); ++i) {
      for (int j = 0; j < world.getWidth(); ++j)
        System.out.format("%3d ", priorities[j][i]);
      System.out.println();
    }
  }
}
