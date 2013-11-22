import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 1:26 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TrooperStrategyAdapter implements ITrooperStrategy {
  protected final Trooper self;

  protected TrooperStrategyAdapter(Trooper self) {
    this.self = self;
  }

}
