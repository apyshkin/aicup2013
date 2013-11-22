/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
interface ITrooperStrategy
{
  Action setActionUnderTactics(AttackTactics attackTactics);
  Action setActionUnderTactics(PatrolTactics patrolTactics);
}
