import model.TrooperStance;

public class TrooperMemorizer {
  private final TrooperModel trooper;
  private int xOld;
  private int yOld;
  private TrooperStance stanceOld;

  private int CHECKING_COUNTER = 0;

  public TrooperMemorizer(TrooperModel trooper) {
    this.trooper = trooper;
  }

  public void memorize() {
    assert (CHECKING_COUNTER == 0);
    xOld = trooper.getX();
    yOld = trooper.getY();
    stanceOld = trooper.getStance();
    ++CHECKING_COUNTER;
  }

  public void reset() {
    assert (CHECKING_COUNTER == 1);
    trooper.setX(xOld);
    trooper.setY(yOld);
    trooper.setStance(stanceOld);
    --CHECKING_COUNTER;
  }

}
