package strategy.exceptions;

import model.ActionType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidActionException extends Exception {
  ActionType actionType;
  public InvalidActionException(ActionType actionType) {
    this.actionType = actionType;
  }

  @Override
  public String getMessage() {
    return "InvalidActionException thrown, trying to perform " + actionType;
  }
}
