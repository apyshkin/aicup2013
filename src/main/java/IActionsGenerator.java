import java.util.ArrayList;


public interface IActionsGenerator {
  public ArrayList<Pair<AbstractAction, IActionParameters>> updateActionParametersWithTrooper(TrooperModel trooper);
}
