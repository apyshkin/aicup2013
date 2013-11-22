import model.TrooperType;

public class HealActionChecker extends ActionChecker {
  public HealActionChecker(Environment env) {
    super(env);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel self) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    if (self.getType() != TrooperType.FIELD_MEDIC)
      return false;
    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (!checkCellIsWithinBoundaries(moveParams.getX(), moveParams.getY()))
      return false;
    if (!checkCellsAreNeighboursOrTheSame(moveParams.getX(), moveParams.getY(), self.getX(), self.getY()))
      return false;
    if (checkCellHasNoMen(moveParams.getX(), moveParams.getY()))
      return false;

    return true;
  }

  private boolean checkCellsAreNeighboursOrTheSame(int x, int y, int x1, int y1) {
    return checkCellsAreNeighbours(x, y, x1, y1) || (x == x1 && y == y1);
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getFieldMedicHealCost();
  }
}
