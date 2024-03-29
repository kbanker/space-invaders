import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;

/**
Parent class for all enemy entities
@author Kush Banker and Jack Basinet
@version 11.14.19
*/
abstract class Enemy
{
  protected int enemyX;
  protected int enemyY;
  protected int enemyWidth;
  protected int enemyHeight;

  protected int health;

  protected int meleeDamage;
  protected int earthDamage;

  protected int shotTimer;
  protected int shotFrequency;

  protected Rectangle bounds = null;

  protected String imgFileName;
  protected BufferedImage image;

  // Variables of movement counters
  protected int moveCount;
  protected int numEns;

  protected int speed;

  // Array of bullets
  protected ArrayList<EnemyBullet> bullets = new ArrayList<EnemyBullet>();

  // Constructor
  public Enemy(int x, int y, int width, int height, String imageFile)
  {
    enemyX = x;
    enemyY = y;
    enemyWidth = width;
    enemyHeight = height;

    imgFileName = imageFile;

    shotTimer = 0;
    shotFrequency = 1500;

    bounds = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);

    this.loadImage();
  }

  public void updateEnemy(int tickMS)
  {
    this.enemyMove(speed);
    this.tick(tickMS);

    if(this.getTimer() > this.getShotFrequency()) { this.shoot(); }
  }

  // Manage enemy image processing
  public void loadImage()
  {
    BufferedImage enemyImg = null;
    try
    {
       enemyImg = ImageIO.read(new File(imgFileName));
    } catch (IOException e) {}

    image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

    Graphics2D imgG = image.createGraphics();
    imgG.drawImage(enemyImg, 0, 0, null);
  }
  public BufferedImage getImage()
  {
    return image;
  }

  // Returns the bounding Box
  public Rectangle getBounds()
  {
    return bounds = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
  }

  public int getTimer()
  {
    return shotTimer;
  }
  public void tick(int ms)
  {
    shotTimer += ms;
  }
  public void resetTimer()
  {
    shotTimer = 0;
  }

  // Returns the shot frequency in ms
  public int getShotFrequency()
  {
    return shotFrequency;
  }

  public int getX()
  {
    return enemyX;
  }
  public int getY()
  {
    return enemyY;
  }

  public int getWidth()
  {
    return enemyWidth;
  }
  public int getHeight()
  {
    return enemyHeight;
  }

  public void setX(int x)
  {
    enemyX = x;
  }
  public void setY(int y)
  {
    enemyY = y;
  }

  // Enemy movement
  public void enemyMove(int speed)
  {
    if(moveCount >= 100) { moveCount = 0; }
    if( (moveCount >= 20 && moveCount < 30) || (moveCount >= 70 && moveCount < 80) )
    {
       this.setY(this.getY() + speed);
       moveCount++;
    }
    else if(moveCount < 20 || moveCount >= 80)
    {
       this.setX(this.getX() - speed);
       moveCount++;
    }
    else if(moveCount < 70)
    {
       this.setX(this.getX() + speed);
       moveCount++;
    }
  }

  // Manage enemy bullets
  abstract public void shoot();
  public ArrayList<EnemyBullet> getBullets()
  {
    return bullets;
  }
  public void removeBullet(EnemyBullet b)
  {
    bullets.remove(b);
  }

  public int getMeleeDamage()
  {
    return meleeDamage;
  }
  public int getEarthDamage()
  {
    if(earthDamage == 0)
    {
      return meleeDamage;
    }
    else
    {
      return earthDamage;
    }

  }

  // Enemy health and hurt
  public int getHealth()
  {
    return health;
  }
  public void hurt(int damage)
  {
    health -= damage;
  }
}

/**
Alien: basic enemy type
*/
class Alien extends Enemy
{
  public static final int ALIEN_WIDTH = 32;
  public static final int ALIEN_HEIGHT = 32;

  public static final String IMG_FILE_NAME = "img/Alien.png";

  // Constructor
  public Alien(int x, int y)
  {
    super(x, y, ALIEN_WIDTH, ALIEN_HEIGHT, IMG_FILE_NAME);

    meleeDamage = 1;
    health = 2;
    speed = 5;

    shotFrequency = 1500 + (int) (Math.random() * 2000);
  }

  public void shoot()
  {
    EnemyBullet enBullet = new EnemyBullet(this.getX() + this.getWidth()/2, this.getY() + this.getHeight());
    bullets.add(enBullet);

    SoundHandler.playSound("sound/alshoot.wav");

    this.resetTimer();
  }
}

/**
Twin: basic enemy type with two bullets
*/
class Twin extends Enemy
{
  public static final int TWIN_WIDTH = 32;
  public static final int TWIN_HEIGHT = 32;

  public static final String IMG_FILE_NAME = "img/Twin.png";

  // Constructor
  public Twin(int x, int y)
  {
    super(x, y, TWIN_WIDTH, TWIN_HEIGHT, IMG_FILE_NAME);

    meleeDamage = 1;
    health = 2;
    speed = 5;

    shotFrequency = 2500 + (int) (Math.random() * 2000);

  }

  public void shoot()
  {
    int centerX = this.getX() + this.getWidth()/2;
    EnemyBullet enBullet1 = new EnemyBullet(centerX + 10, this.getY() + this.getHeight());

    EnemyBullet enBullet2 = new EnemyBullet(centerX - 10, this.getY() + this.getHeight());

    bullets.add(enBullet1);
    bullets.add(enBullet2);

    SoundHandler.playSound("sound/twshoot.wav");

    this.resetTimer();
  }
}

/**
Cannon: enemy type that fires 1 big bullet
*/
class Cannon extends Enemy
{
  public static final int CANNON_WIDTH = 40;
  public static final int CANNON_HEIGHT = 44;

  public static final String IMG_FILE_NAME = "img/Cannon.png";

  // Constructor
  public Cannon(int x, int y)
  {
    super(x, y, CANNON_WIDTH, CANNON_HEIGHT, IMG_FILE_NAME);

    meleeDamage = 2;
    health = 6;
    speed = 2;

    shotFrequency = 3500 + (int) (Math.random() * 1000);
  }

  public void shoot()
  {
    EnemyMissile enMissile = new EnemyMissile(getX() + this.getWidth()/2, this.getY() + this.getHeight());
    bullets.add(enMissile);

    SoundHandler.playSound("sound/cashoot.wav");

    this.resetTimer();
  }
}

/**
Mini: small fast and in waves
*/
class Mini extends Enemy
{
  public static final int WIDTH = 24;
  public static final int HEIGHT = 24;

  public static final String IMG_FILE_NAME = "img/Mini.png";

  // Constructor
  public Mini(int x, int y)
  {
    super(x, y, WIDTH, HEIGHT, IMG_FILE_NAME);

    meleeDamage = 1;
    health = 1;
    speed = 10;
  }

  public void shoot(){}

}

/**
Hearty: basic enemy type that when killed gives life
  only does damage through bullets
*/
class Hearty extends Enemy
{
  public static final int HEARTY_WIDTH = 32;
  public static final int HEARTY_HEIGHT = 32;

  public static final String IMG_FILE_NAME = "img/Hearty.png";

  // Constructor
  public Hearty(int x, int y)
  {
    super(x, y, HEARTY_WIDTH, HEARTY_HEIGHT, IMG_FILE_NAME);

    meleeDamage = 0;
    health = 3;
    speed = 6;

    shotFrequency = 1500 + (int) (Math.random() * 2000);
  }

  public void shoot()
  {
    EnemyBullet enBullet = new EnemyBullet(this.getX() + this.getWidth()/2, this.getY() + this.getHeight());
    bullets.add(enBullet);

    SoundHandler.playSound("sound/alshoot.wav");

    this.resetTimer();
  }
}

/**
Spike: goes straight down and does more damage if it hits earth
*/
class Spike extends Enemy
{
  public static final int WIDTH = 32;
  public static final int HEIGHT = 32;

  public static final String IMG_FILE_NAME = "img/Spike.png";

  // Constructor
  public Spike(int x, int y)
  {
    super(x, y, WIDTH, HEIGHT, IMG_FILE_NAME);

    earthDamage = 4;
    meleeDamage = 2;
    health = 9;
    speed = 4;
  }

  public void shoot(){}

  @Override
  public void enemyMove(int speed)
  {
    enemyY += speed;
  }
}

/**
Tank: beefee unit and spwns two tankis when dies
*/
class Tank extends Enemy
{
  public static final int WIDTH = 40;
  public static final int HEIGHT = 40;

  public static final String IMG_FILE_NAME = "img/Tank.png";

  // Constructor
  public Tank(int x, int y)
  {
    super(x, y, WIDTH, HEIGHT, IMG_FILE_NAME);

    earthDamage = 0;
    meleeDamage = 1;
    health = 13;
    speed = 2;
  }

  public void shoot(){}

  @Override
  public void enemyMove(int speed)
  {
    enemyY += speed;
  }
}

/**
Tanki2: first slow mini spawned by tank
*/
class Tanki extends Enemy
{
  public static final int WIDTH = 24;
  public static final int HEIGHT = 24;

  private int imgFileC;

  // Constructor
  public Tanki(int x, int y, int imgFileNum)
  {
    super(x, y, WIDTH, HEIGHT, "img/tanki" + imgFileNum + ".png");
    imgFileC = imgFileNum;

    meleeDamage = 1;
    health = 1;
    speed = 4;
  }

  @Override
  public void enemyMove(int speed)
  {
    int mod;
    if(imgFileC == 1) { mod = 1; }
    else { mod = -1; }
    if(moveCount >= 44) { moveCount = 0; }

    if(moveCount < 17)
    {
       this.setX(this.getX() - (mod*speed));
       moveCount++;
    }
    else if( moveCount >= 17 && moveCount < 22 )
    {
       this.setY(this.getY() + speed);
       moveCount++;
    }
    else if(moveCount >= 22 && moveCount < 39)
    {
       this.setX(this.getX() + (mod*speed));
       moveCount++;
    }
    else if( moveCount >= 39)
    {
       this.setY(this.getY() + speed);
       moveCount++;
    }
  }

  public void shoot()
  {
    EnemyBullet enBullet = new EnemyBullet(this.getX() + this.getWidth()/2, this.getY() + this.getHeight());
    bullets.add(enBullet);

    SoundHandler.playSound("sound/alshoot.wav");

    this.resetTimer();
  }
}
