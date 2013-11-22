import model.TrooperType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidTrooperTypeException extends Exception {
  public InvalidTrooperTypeException() {
    super();
  }

  public InvalidTrooperTypeException(String message) {
    super(message);
  }
}
