public class UseMedkitActionParameters extends HealActionParameters {

  public UseMedkitActionParameters(TrooperModel patient) {
    super(patient);
  }

  @Override
  public String toString() {
    return "using medkit on " + this.patient + " at " + getX() + " " + getY();
  }
}
