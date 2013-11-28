import model.Trooper;
import model.TrooperStance;
import model.TrooperType;

public class TrooperModel extends UnitModel implements Cloneable {
  private long playerId;
  private int teammateIndex;
  private boolean teammate;
  private TrooperType type;
  private TrooperStance stance;
  private int hitpoints;
  private int maximalHitpoints;
  private int actionPoints;
  private int initialActionPoints;
  private double visionRange;
  private double shootingRange;
  private int shootCost;
  private int standingDamage;
  private int kneelingDamage;
  private int proneDamage;
  private int damage;
  private boolean holdingGrenade;
  private boolean holdingMedkit;
  private boolean holdingFieldRation;

  public TrooperModel(
          long id, int x, int y, long playerId,
          int teammateIndex, boolean teammate, TrooperType type, TrooperStance stance,
          int hitpoints, int maximalHitpoints, int actionPoints, int initialActionPoints,
          double visionRange, double shootingRange, int shootCost,
          int standingDamage, int kneelingDamage, int proneDamage,
          boolean holdingGrenade, boolean holdingMedkit, boolean holdingFieldRation) {
    super(id, x, y);
    this.playerId = playerId;
    this.teammateIndex = teammateIndex;
    this.teammate = teammate;
    this.type = type;
    this.stance = stance;
    this.hitpoints = hitpoints;
    this.maximalHitpoints = maximalHitpoints;
    this.actionPoints = actionPoints;
    this.initialActionPoints = initialActionPoints;
    this.visionRange = visionRange;
    this.shootingRange = shootingRange;
    this.shootCost = shootCost;
    this.standingDamage = standingDamage;
    this.kneelingDamage = kneelingDamage;
    this.proneDamage = proneDamage;
    this.damage = damage;
    this.holdingGrenade = holdingGrenade;
    this.holdingMedkit = holdingMedkit;
    this.holdingFieldRation = holdingFieldRation;
  }

  public TrooperModel(Trooper trooper) {
    this(trooper.getPlayerId(), trooper.getX(), trooper.getY(), trooper.getPlayerId(), trooper.getTeammateIndex(),
            trooper.isTeammate(), trooper.getType(), trooper.getStance(), trooper.getHitpoints(), trooper.getMaximalHitpoints(),
            trooper.getActionPoints(), trooper.getInitialActionPoints(), trooper.getVisionRange(), trooper.getShootingRange(),
            trooper.getShootCost(), trooper.getStandingDamage(), trooper.getKneelingDamage(), trooper.getProneDamage(),
            trooper.isHoldingGrenade(), trooper.isHoldingMedikit(), trooper.isHoldingFieldRation());
  }

  public TrooperModel(TrooperModel trooper) {
    this(trooper.getPlayerId(), trooper.getX(), trooper.getY(), trooper.getPlayerId(), trooper.getTeammateIndex(),
            trooper.isTeammate(), trooper.getType(), trooper.getStance(), trooper.getHitpoints(), trooper.getMaximalHitpoints(),
            trooper.getActionPoints(), trooper.getInitialActionPoints(), trooper.getVisionRange(), trooper.getShootingRange(),
            trooper.getShootCost(), trooper.getStandingDamage(), trooper.getKneelingDamage(), trooper.getProneDamage(),
            trooper.isHoldingGrenade(), trooper.isHoldingMedkit(), trooper.isHoldingFieldRation());
  }

  public long getPlayerId() {
    return playerId;
  }

  public int getTeammateIndex() {
    return teammateIndex;
  }

  public boolean isTeammate() {
    return teammate;
  }

  public TrooperType getType() {
    return type;
  }

  public TrooperStance getStance() {
    return stance;
  }

  public void setStance(TrooperStance stance) {
    this.stance = stance;
  }

  public int getHitpoints() {
    return hitpoints;
  }

  public void setHitpoints(int hp) {
    hitpoints = hp;
  }

  public int getMaximalHitpoints() {
    return maximalHitpoints;
  }

  public int getActionPoints() {
    return actionPoints;
  }

  public void setActionPoints(int ap) {
    actionPoints = ap;
  }

  public int getInitialActionPoints() {
    return initialActionPoints;
  }

  public double getVisionRange() {
    return visionRange;
  }

  public double getShootingRange() {
    return shootingRange;
  }

  public int getShootCost() {
    return shootCost;
  }

  public int getStandingDamage() {
    return standingDamage;
  }

  public int getKneelingDamage() {
    return kneelingDamage;
  }

  public int getProneDamage() {
    return proneDamage;
  }

  public int getDamage(TrooperStance stance) {
    switch (stance) {
      case PRONE:
        return proneDamage;
      case KNEELING:
        return kneelingDamage;
      case STANDING:
        return standingDamage;
      default:
        throw new IllegalArgumentException("Unsupported stance: " + stance + '.');
    }
  }

  public int getDamage() {
    return getDamage(stance);
  }

  public boolean isHoldingGrenade() {
    return holdingGrenade;
  }

  public boolean isHoldingMedkit() {
    return holdingMedkit;
  }

  public boolean isHoldingFieldRation() {
    return holdingFieldRation;
  }

  public void lowStance() {
    switch (stance) {
      case KNEELING:
        stance = TrooperStance.PRONE;
        break;
      case STANDING:
        stance = TrooperStance.KNEELING;
        break;
    }
  }

  public void raiseStance() {
    switch (stance) {
      case KNEELING:
        stance = TrooperStance.STANDING;
        break;
      case PRONE:
        stance = TrooperStance.KNEELING;
        break;
    }
  }

  public void move(int x, int y, TrooperStance stance) {
    setX(x);
    setY(y);
    setStance(stance);
  }

  @Override
  public boolean equals(Object object) {
    assert (object instanceof TrooperModel);
    TrooperModel another = (TrooperModel) object;
    return  getX() == another.getX() && getY() == another.getY() && type == another.type && stance == another.stance &&
            hitpoints == another.hitpoints && playerId == another.playerId && actionPoints == another.actionPoints &&
            holdingFieldRation == another.holdingFieldRation && holdingGrenade == another.holdingGrenade &&
            holdingMedkit == another.holdingMedkit;
  }

  @Override
  public String toString() {
    return "trooper(" + getHitpoints() + "):" + getType() + " at (" + getX() + "," + getY() + ") ap " + getActionPoints();
  }

  public void useMedkit() {
    holdingMedkit = false;
  }

  public void obtainMedkit() {
    holdingMedkit = true;
  }

  public void throwGrenade() {
    holdingGrenade = false;
  }

  public void obtainGrenade() {
    holdingGrenade = true;
  }

  public void eatFieldRation() {
    holdingFieldRation = false;
  }

  public void obtainFieldRation() {
    holdingFieldRation = true;
  }

  @Override
  public TrooperModel clone() {
    return new TrooperModel(this);
  }

  public void turnToEnemy() {
    teammate = false;
    playerId++;
  }
}
