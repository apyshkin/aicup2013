import model.ActionType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidActionException extends Exception {
  public InvalidActionException(ActionType actionType) {
    super(actionType.toString());
  }

  public InvalidActionException() {
    super();
  }
}
