/**
 * Created by alexeyka on 12/4/13.
 */
public class PotentialCoeffPack {
  private final int kBonusPath;
  private final int kVisionPath;

  public PotentialCoeffPack(int kBonusPath, int kVisionPath) {
    this.kBonusPath = kBonusPath;
    this.kVisionPath = kVisionPath;
  }

  public int getkBonusPath() {
    return kBonusPath;
  }

  public int getkVisionPath() {
    return kVisionPath;
  }
}
