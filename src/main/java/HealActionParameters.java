/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/23/13
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class HealActionParameters extends DestinationActionParameters {

  private final TrooperModel patient;

  public HealActionParameters(TrooperModel patient) {
    super(patient.getX(), patient.getY());
    this.patient = patient;
  }

  @Override
  public String toString() {
    return "healing " + patient + " at " + getX() + " " + getY();
  }

  public TrooperModel getPatient() {
    return patient;
  }
}
