import model.Player;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerState {
  private final Player player;
  private final int teamSize;
  public ArrayList<TrooperModel> deadTroopers;
  private boolean playerDead = false;

  public PlayerState(Player player, int teamSize) {
    this.player = player;
    this.teamSize = teamSize;
    deadTroopers = new ArrayList<>();
  }

  public int troopersDeadCount() {
    return deadTroopers.size();
  }

  public void killTrooper(TrooperModel trooper) {
    deadTroopers.add(trooper);
    if (troopersDeadCount() == teamSize)
      playerDead = true;
  }

  public boolean isPlayerDead() {
    return (player.isStrategyCrashed() || playerDead);
  }

  public void setPlayerDead() {
    playerDead = true;
  }

  public Player getPlayer() {
    return player;
  }

}
