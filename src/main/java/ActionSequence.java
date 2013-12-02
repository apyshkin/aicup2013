/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/30/13
 * Time: 3:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActionSequence {
  private Pair<AbstractAction, IActionParameters>[] actions;
  private int size;

  public ActionSequence(int size) {
    actions = new Pair[size];
    this.size = size;
  }

  public void set(int index, Pair<AbstractAction, IActionParameters> pair) {
    assert index < size;
    actions[index] = pair;
  }

  public Pair<AbstractAction, IActionParameters> get(int index) {
    assert index < size;
    return actions[index];
  }

  public Pair<AbstractAction, IActionParameters> getFirst() {
    return get(0);
  }

  public void trim(int size) {
    this.size = size;
  }

  private int iterationCounter = 0;

  public Pair<AbstractAction, IActionParameters> next() {
    return get(iterationCounter++);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    int count = 0;
    for (Pair<AbstractAction, IActionParameters> pair : actions) {
      if (count == size)
        break;
      builder.append(pair.getKey() + " " + pair.getValue() + "\n");
      ++count;
    }
    return builder.toString();
  }
}
