/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:23 PM
 * To change this template use File | Settings | File Templates.
 */
interface ITactics {
  void setAction(ITrooperStrategy trooper);
  CellPriorities generateCellPriorities();
}
