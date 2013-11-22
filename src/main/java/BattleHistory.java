import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 8:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class BattleHistory {
  private ArrayList<Environment> history;

  public BattleHistory() {
    history = new ArrayList<>();
  }

  public void add(Environment environment) {
    history.add(environment);
  }

  public Environment get(int index) {
    assert(index < history.size());
    return history.get(index);
  }

  public Environment getLast() {
    return history.get(history.size() - 1);
  }

  public int size() {
    return history.size();
  }
}
