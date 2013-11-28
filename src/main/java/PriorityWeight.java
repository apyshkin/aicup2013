/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/28/13
 * Time: 12:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityWeight {
  private final IPriority priority;
  private final int priorityCoefficient;

  public PriorityWeight(IPriority priority, int priorityCoefficient) {
    this.priority = priority;
    this.priorityCoefficient = priorityCoefficient;
  }

  public int countCellWeight(int x, int y, int stance) {
    if (priorityCoefficient == 0)
      return 0;

    return priorityCoefficient * priority.getPriority(x, y, stance);
  }

  @Override
  public String toString() {
    return "Priority weight for " + priority.getClass().getSimpleName() + ": coefficient = " + priorityCoefficient;
  }
}
