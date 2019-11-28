import java.lang.*;

/**
Class to track bullets
@author Kush Banker
@version ur mom
*/
class Bullet
{
  protected int bulletX;
  protected int bulletY;

  protected int bulletDiam;
  protected int bulletSpeed;

  protected int bulletDamage;

  protected long timeShot;

  // Constructor
  public Bullet(int x, int y, int d, int s, int damage)
  {
    bulletX = x;
    bulletY = y;

    bulletDiam = d;
    bulletSpeed = s;

    bulletDamage = damage;

    timeShot = System.currentTimeMillis();
  }

  public void updateBullet()
  {
    bulletY += bulletSpeed;
  }

  public void changeX(int change)
  {
    bulletX += change;
  }
  public void changeY(int change)
  {
    bulletY += change;
  }

  public int getX()
  {
    return bulletX;
  }
  public int getY()
  {
    return bulletY;
  }

  public long getTimeShot()
  {
    return timeShot;
  }

  public void setX(int x)
  {
    bulletX = x;
  }
  public void setY(int y)
  {
    bulletY = y;
  }

  public int getDiam()
  {
    return bulletDiam;
  }
  public int getSpeed()
  {
    return bulletSpeed;
  }

  public int getDamage()
  {
    return bulletDamage;
  }
}

/**
Subclass of Bullets for use by Enemies
@author Kush Banker
@version 15.11.19
*/
class EnemyBullet extends Bullet
{
  public static final int ENEMY_BULLET_SIZE = 10;
  public static final int ENEMY_BULLET_SPEED = 20;

  public static final int DAMAGE = 1;

  // Constructor with set size and speed
  public EnemyBullet(int x, int y)
  {
    super(x, y, ENEMY_BULLET_SIZE, ENEMY_BULLET_SPEED, DAMAGE);
  }
  // Constructor with size and speed params
  public EnemyBullet(int x, int y, int d, int s, int damage)
  {
    super(x, y, d, s, damage);
  }

}

/**
Subclass of Bullets for use by Enemies
@author Kush Banker and Jack Basinet
@version 15.11.19
*/
class EnemyMissile extends EnemyBullet
{
  public static final int MISSILE_SIZE = 20;
  public static final int MISSILE_SPEED = 10;
  public static final int DAMAGE = 3;

  // Constructor
  public EnemyMissile(int x, int y)
  {
    super(x, y, MISSILE_SIZE, MISSILE_SPEED, DAMAGE);
  }
}

/**
Subclass of Bullets for use by the player
@author Kush Banker
@version 15.11.19
*/
class PlayerBullet extends Bullet
{

  // Constructor
  public PlayerBullet(int x, int y, int size, int speed, int damage)
  {
    super(x, y, size, speed, damage);
  }
}
