import model.CellType;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BattleMap {
  private World world;
  private Cell[][] cells;
  private int[][][][] distances;


  public BattleMap(World world) {
    this.world = world;
    init();
  }

  private void init() {
    cells = new Cell[world.getWidth()][world.getHeight()];
    CellType[][] worldCells = world.getCells();
    for (int i = 0; i < worldCells.length; ++i)
      for (int j = 0; j < worldCells[0].length; ++j)
        cells[i][j] = new Cell(worldCells[i][j], 0);
  }

  public void visitCell(int x, int y, int time) {
    cells[x][y].update(time);
  }

  public Cell getCell(int x, int y) {
    return cells[x][y];
  }

}

class Cell {
  CellType cellType;
  int timeOfLastVisit;

  public Cell(CellType cellType, int timeOfLastVisit) {
    this.cellType = cellType;
    this.timeOfLastVisit = timeOfLastVisit;
  }

  public void update(int time) {
    assert(time > timeOfLastVisit);
    timeOfLastVisit = time;
  }
}
