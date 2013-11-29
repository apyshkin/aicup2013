import java.util.ArrayList;

class Pair<K, V> {
  private final K k;
  private final V v;

  public Pair(K k, V v) {
    this.k = k;
    this.v = v;
  }

  public K getKey() {
    return k;
  }

  public V getValue() {
    return v;
  }
}


public interface IActionsGenerator {
  public ArrayList<Pair<Action, IActionParameters>> updateActionParametersWithTrooper(TrooperModel trooper);
}
