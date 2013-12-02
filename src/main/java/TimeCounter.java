/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeCounter {
  private double currentTime;
  public TimeCounter() {
    currentTime = System.currentTimeMillis();
  }

  public void status(String spender) {
    System.out.println("time for " + spender + " " + (-currentTime + System.currentTimeMillis()));
    reset();
  }

  public void reset() {
    currentTime = System.currentTimeMillis();
  }

}
