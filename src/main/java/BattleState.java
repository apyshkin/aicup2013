import model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BattleState {
  private final HashMap<Long, PlayerState> playerStates;
  private final long myId;
  private final int playersTotal;

  public BattleState(Player[] players, long myId, int teamSize) {
    playerStates = new HashMap<>();
    playersTotal = players.length;
    for (Player player : players) {
      playerStates.put(player.getId(), new PlayerState(player, teamSize));
    }
    this.myId = myId;
  }

  public PlayerState getPlayerState(long id) {
    return playerStates.get(id);
  }

  public int getDeadPlayersCount() {
    int count = 0;
    for (PlayerState state : playerStates.values()) {
      if (state.isPlayerDead())
        ++count;
    }
    return count;
  }

  public int getPlayersTotal() {
    return playersTotal;
  }

  public void setPlayerDead(Player player) {
    getPlayerState(player.getId()).setPlayerDead();
  }

  public long getMyId() {
    return myId;
  }

  public void setTrooperDead(TrooperModel trooper) {
    PlayerState state = getPlayerState(trooper.getPlayerId());
    state.killTrooper(trooper);
  }
}
