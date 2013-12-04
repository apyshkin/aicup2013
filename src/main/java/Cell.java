import model.Bonus;
import model.CellType;

public class Cell {
  private CellType cellType;
  private Bonus bonus = null;
  private int timeLastSeen;

  public Cell(CellType cellType, int timeLastSeen) {
    this.cellType = cellType;
    this.timeLastSeen = timeLastSeen;
  }

  int getTimeLastSeen() {
    return timeLastSeen;
  }

  public boolean hasBonus() {
    return bonus != null;
  }

  public Bonus getBonus() {
    return bonus;
  }

  void setBonus(Bonus bonus) {
    this.bonus = bonus;
  }

  public void update(int time) {
    assert time >= timeLastSeen;
    timeLastSeen = time;
  }

  public boolean isActual(int currentTime) {
    return currentTime <= timeLastSeen + 1;
  }

  public void invalidate() {
    timeLastSeen = 0;
  }

  public CellType getType() {
    return cellType;
  }

  public int getActuality(int currentTime) {
    return (currentTime - timeLastSeen + 1);
  }
}
