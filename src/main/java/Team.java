import model.TrooperType;

import java.util.ArrayList;

public class Team {
  private final ArrayList<TrooperModel> myTroopers;
  private final TrooperModel leader;
  private static final TrooperType[] LEADER_ORDER = {
          TrooperType.COMMANDER,
          TrooperType.SOLDIER,
          TrooperType.SNIPER,
          TrooperType.SCOUT,
          TrooperType.FIELD_MEDIC
  };

  public Team(ArrayList <TrooperModel> myTroopers) {
    this.myTroopers = myTroopers;
    leader = findLeader();
  }

  private TrooperModel findLeader() {
    TrooperModel leader = null;
    for (TrooperType leaderType : LEADER_ORDER)
      if (isAlive(leaderType)) {
        leader = getMyTrooper(leaderType);
        return leader;
      }

    assert (leader != null);
    return null;
  }

  public TrooperModel getMyTrooper(TrooperType trooperType) {
    for (TrooperModel trooper : myTroopers)
      if (trooper.getType() == trooperType)
        return trooper;

    assert false;
    return null;
  }

  public ArrayList<TrooperModel> getMyTroopers() {
    return myTroopers;
  }

  public boolean isAlive(TrooperType trooperType) {
    for (TrooperModel trooper : myTroopers)
      if (trooper.getType() == trooperType)
        return true;

    return false;
  }

  public long getMyId() {
    ArrayList<TrooperModel> myTroopers = getMyTroopers();
    assert (!myTroopers.isEmpty());
    return myTroopers.get(0).getPlayerId();
  }

  public TrooperModel getLeader() {
    return leader;
  }
}
