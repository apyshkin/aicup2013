/**
 * Created by alexeyka on 12/4/13.
 */
public class MapCellFactory {
  private final int width;
  private final int height;

  public MapCellFactory(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public MapCell createMapCell(int x, int y) {
    return new MapCell(width, height, x, y);
  }
}
