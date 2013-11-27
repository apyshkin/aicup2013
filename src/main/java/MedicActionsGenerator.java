public class MedicActionsGenerator extends TrooperActionsGenerator {
  public MedicActionsGenerator(Environment environment) {
    super(environment);
  }

  protected void init(TrooperModel healer) {
    super.init(healer);

    for (TrooperModel patient : environment.getMyTroopers()) {
      HealActionParameters healActionParams = new HealActionParameters(patient);
      actionsList.add(new Pair<Action, IActionParameters>(new HealAction(environment), healActionParams));
    }
  }

}
