import java.util.Arrays;

import static java.lang.StrictMath.min;

import model.*;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimulatedWorld {
  private int moveIndex;
  private int width;
  private int height;
  private Player[] players;
  private Trooper[] troopers;
  private Bonus[] bonuses;
  private CellType[][] cells;
  private boolean[] cellVisibilities;

  public SimulatedWorld(
          int moveIndex, int width, int height, Player[] players, Trooper[] troopers, Bonus[] bonuses,
          CellType[][] cells, boolean[] cellVisibilities) {
    this.moveIndex = moveIndex;
    this.width = width;
    this.height = height;
    this.players = Arrays.copyOf(players, players.length);
    this.troopers = Arrays.copyOf(troopers, troopers.length);
    this.bonuses = Arrays.copyOf(bonuses, bonuses.length);

    this.cells = new CellType[width][];
    for (int x = 0; x < width; ++x) {
      this.cells[x] = Arrays.copyOf(cells[x], cells[x].length);
    }

    this.cellVisibilities = cellVisibilities;
  }

  public SimulatedWorld(World world) {
    this(world.getMoveIndex(), world.getWidth(), world.getHeight(), world.getPlayers(),
            world.getTroopers(), world.getBonuses(), world.getCells(), world.getCellVisibilities());
  }

  /**
   * @return Возвращает номер текущего хода.
   */
  public int getMoveIndex() {
    return moveIndex;
  }

  /**
   * @return Возвращает ширину мира в клетках.
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return Возвращает высоту мира в клетках.
   */
  public int getHeight() {
    return height;
  }

  /**
   * @return Возвращает список игроков (в случайном порядке).
   *         Объекты, задающие игроков, пересоздаются перед каждым вызовом {@code Strategy.move()}.
   */
  public Player[] getPlayers() {
    return Arrays.copyOf(players, players.length);
  }

  /**
   * @return Возвращает список видимых юнитами игрока бойцов (в случайном порядке),
   *         включая бойца стратегии, вызвавшей этот метод.
   *         Объекты, задающие бойцов, пересоздаются перед каждым вызовом {@code Strategy.move()}.
   */
  public Trooper[] getTroopers() {
    return Arrays.copyOf(troopers, troopers.length);
  }

  /**
   * @return Возвращает список видимых юнитами игрока бонусов (в случайном порядке).
   *         Объекты, задающие бонусы, пересоздаются перед каждым вызовом {@code Strategy.move()}.
   */
  public Bonus[] getBonuses() {
    return Arrays.copyOf(bonuses, bonuses.length);
  }

  /**
   * @return Возвращает двумерный массив типов клеток игрового поля,
   *         где первое измерение --- это координата X, а второе --- Y.
   */
  public CellType[][] getCells() {
    CellType[][] copiedCells = new CellType[cells.length][];
    for (int x = 0; x < cells.length; ++x) {
      copiedCells[x] = Arrays.copyOf(cells[x], cells[x].length);
    }
    return copiedCells;
  }

  /**
   * Возвращает массив досягаемости различных клеток игрового мира из других клеток без учёта расстояния
   * между ними.
   * <p/>
   * Боец в клетке {@code (objectX, objectY)} является досягаемым для бойца в клетке {@code (viewerX, viewerY)},
   * если и только если {@code cellVisibilities[viewerX * height * width * height * stanceCount
   * + viewerY * width * height * stanceCount
   * + objectX * height * stanceCount
   * + objectY * stanceCount
   * + minStanceIndex]} равно {@code true}, где {@code height} --- высота мира в клетках,
   * {@code width} --- ширина мира в клетках, {@code stanceCount} --- количество различных значений перечисления
   * {@code TrooperStance}, а {@code minStanceIndex} --- минимальный из индексов стоек двух указанных бойцов
   * (стойка {@code TrooperStance.PRONE} имеет индекс {@code 0}).
   *
   * @return Возвращает массив досягаемости.
   */
  public boolean[] getCellVisibilities() {
    return Arrays.copyOf(cellVisibilities, cellVisibilities.length);
  }

  /**
   * Метод проверяет, является ли юнит, находящийся в клетке с координатами
   * ({@code objectX}, {@code objectY}) в стойке {@code objectStance},
   * досягаемым для юнита, находящегося в клетке с координатами
   * ({@code viewerX}, {@code viewerY}) в стойке {@code viewerStance}.
   * Может использоваться как для проверки видимости, так и для проверки возможности стрельбы.
   * <p/>
   * При проверке видимости бонуса его высота считается равной высоте бойца в стойке {@code TrooperStance.PRONE}.
   *
   * @param maxRange     Дальность обзора/стрельбы наблюдающего юнита ({@code viewer}).
   * @param viewerX      X-координата наблюдающего юнита ({@code viewer}).
   * @param viewerY      Y-координата наблюдающего юнита ({@code viewer}).
   * @param viewerStance Стойка наблюдающего юнита ({@code viewer}).
   * @param objectX      X-координата наблюдаемого юнита ({@code object}).
   * @param objectY      Y-координата наблюдаемого юнита ({@code object}).
   * @param objectStance Стойка наблюдаемого юнита ({@code object}).
   * @return Возвращает {@code true}, если и только если наблюдаемый юнит ({@code object})
   *         является досягаемым для наблюдающего юнита ({@code viewer}).
   */
  public boolean isVisible(
          double maxRange,
          int viewerX, int viewerY, TrooperStance viewerStance,
          int objectX, int objectY, TrooperStance objectStance) {
    int minStanceIndex = min(viewerStance.ordinal(), objectStance.ordinal());
    int xRange = objectX - viewerX;
    int yRange = objectY - viewerY;

    return xRange * xRange + yRange * yRange <= maxRange * maxRange
            && cellVisibilities[
            viewerX * height * width * height * StanceCountHolder.STANCE_COUNT
                    + viewerY * width * height * StanceCountHolder.STANCE_COUNT
                    + objectX * height * StanceCountHolder.STANCE_COUNT
                    + objectY * StanceCountHolder.STANCE_COUNT
                    + minStanceIndex
            ];
  }

  private static final class StanceCountHolder {
    private static final int STANCE_COUNT = TrooperStance.values().length;

    private StanceCountHolder() {
      throw new UnsupportedOperationException();
    }
  }
}
