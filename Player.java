import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.lang.*;

/**
Parent class for all player entities
@author Kush Banker and Jack Basinet
@version 11.14.19
*/
class Player
{
  private int playerX;
  private int playerY;
  private int playerWidth;
  private int playerHeight;

  private int playerSpeed;

  private int centerX;
  private int centerY;

  private Rectangle bounds = null;

  private int livesLeft;

  private int playerScore;
  private int playerLevel;

  private int gun;
  private int bulletDelay;
  private int weaponDamage;
  private int bulletSize;
  private int bulletSpeed;

  // Player graphics
  private String imgFileName;
  private BufferedImage image;

  // Array of bullets
  private ArrayList<PlayerBullet> bullets = new ArrayList<PlayerBullet>();

  // Life graphics
  private String imgFileLife;
  private BufferedImage life;

  private int fourCount;

  public Player()
  {
    playerX = 100;
    playerY = 490;
    playerWidth = 55;
    playerHeight = 60;

    playerSpeed = 40;

    livesLeft = 10;

    playerLevel = 1;

    playerScore = 0;

    gun = 1;
    bulletDelay = 200;
    weaponDamage = 2;
    bulletSize = 10;

    imgFileName = "img/player1.png";
    imgFileLife = "img/" + livesLeft + ".png";

    bounds = new Rectangle(playerX, playerY, playerWidth, playerHeight);

    fourCount = 0;

    this.loadImage();
    this.updateLives();
  }
  public void updatePlayer()
  {
    centerX = playerX + playerWidth / 2;
    centerY = playerY;

    if(gun == 1)
    {
      weaponDamage = 2;
      bulletSize = 10;
      bulletSpeed = 20;
      bulletDelay = 200;
    }
    else if(gun == 2)
    {
      weaponDamage = 2;
      bulletSize = 10;
      bulletSpeed = 20;
      bulletDelay = 430;
    }
    else if(gun == 3)
    {
      weaponDamage = 1;
      bulletSize = 6;
      bulletSpeed = 20;
      bulletDelay = 80;
    }

    this.loadImage();
  }

  public void score(int score)
  {
    playerScore += score;

    if(playerScore < 80) { playerLevel = 1; }
    else if(playerScore < 200)
    {
      playerLevel = 2;
      playerWidth = 45;
      playerHeight = 70;
    }
    else if(playerScore < 400)
    {
      playerLevel = 3;
      playerWidth = 45;
      playerHeight = 65;
    }
    else
    {
      playerLevel = 4;
      playerWidth = 35;
      playerHeight = 35;
    }
  }

  // Manage player image processing
  public void loadImage()
  {
    this.updatePlayerArt();
    //floating rainbow orb that can smash through enemies but still takes bulllet damage

    BufferedImage playerImg = null;
    try
    {
       playerImg = ImageIO.read(new File(imgFileName));
    } catch (IOException e) {}

    image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

    Graphics2D imgG = image.createGraphics();
    imgG.drawImage(playerImg, 0, 0, null);
  }
  public BufferedImage getImage()
  {
    return image;
  }
  public void updatePlayerArt()
  {
    if(livesLeft == 0) { SoundHandler.playSound("sound/explode.wav"); }
    if(livesLeft <= 0)
    {
      imgFileName = "img/explode" + livesLeft + ".png";
      livesLeft -= 1;
    }
    if(playerLevel == 4)
    {
      int fourloop = 1;
      fourCount += 1;
      if(fourCount == 2)
      {
        fourloop ++;
        fourCount = 0;
      }
      if(fourloop > 4) { fourloop = 1; }

      if(gun == 4) { imgFileName = "img/player4" + fourloop + ".png"; }
      else { imgFileName = "img/player4p.png"; }
    }

    else if(livesLeft > 7) { imgFileName = "img/player" + playerLevel + ".png"; }
    else if(livesLeft > 3) { imgFileName = "img/player" + playerLevel + "2.png"; }
    else if(livesLeft > 0) { imgFileName = "img/player" + playerLevel + "3.png"; }
    // else if(livesLeft == 0) { imgFileName = "img/e";}
  }

  // Returns the bounding Box
  public Rectangle getBounds()
  {
    return bounds = new Rectangle(playerX, playerY, playerWidth, playerHeight);
  }

  public int getX()
  {
    return playerX;
  }
  public int getY()
  {
    return playerY;
  }

  public int getWidth()
  {
    return playerWidth;
  }
  public int getHeight()
  {
    return playerHeight;
  }

  public int getScore()
  {
    return playerScore;
  }

  public void setX(int x)
  {
    playerX = x;
  }
  public void setY(int y)
  {
    playerY = y;
  }

  public void move(boolean right)
  {
    if(right && centerX < GamePanel.WINDOW_WIDTH) { playerX += playerSpeed; }
    else if(!right && centerX > 0 ){ playerX -= playerSpeed; }
  }

  // Manage player bullets
  public void shoot()
  {
    long lastBulletTime;

    if(bullets.size() > 0) { lastBulletTime = bullets.get(bullets.size() - 1).getTimeShot(); }
    else { lastBulletTime = 0; }

    if(lastBulletTime + bulletDelay < System.currentTimeMillis())
    {
      if(gun == 1 || gun == 3)
      {
        PlayerBullet b = new PlayerBullet(centerX, centerY, bulletSize, -bulletSpeed, weaponDamage);
        bullets.add(b);
      }
      else if(gun == 2)
      {
        PlayerBullet bullet1 = new PlayerBullet(centerX + 10, centerY, bulletSize, -bulletSpeed, weaponDamage);
        PlayerBullet bullet2 = new PlayerBullet(centerX - 10, centerY, bulletSize, -bulletSpeed, weaponDamage);

        bullets.add(bullet1);
        bullets.add(bullet2);
      }
      SoundHandler.playSound("sound/shoot.wav");
    }
  }
  public ArrayList<PlayerBullet> getBullets()
  {
    return bullets;
  }
  public void removeBullet(PlayerBullet b)
  {
    bullets.remove(b);
  }
  public void changeWeapon()
  {
    if(gun >= playerLevel)
    {
      gun = 1;
    }
    else
    {
      gun++;
    }
  }
  public int getGun()
  {
    return gun;
  }
  public int getBulletSize()
  {
    return bulletSize;
  }
  public int getWeaponDamage()
  {
    return weaponDamage;
  }

  // Manage player damage
  public void hurt(int damage)
  {
    livesLeft -= damage;
    imgFileLife = "img/" + livesLeft + ".png";

    SoundHandler.playSound("sound/hit.wav");
    this.updateLives();
  }
  public void gainLives(int amount)
  {
    livesLeft += amount;
    imgFileLife = "img/" + livesLeft + ".png";

    SoundHandler.playSound("");
    this.updateLives();
  }
  public int getLives()
  {
    return livesLeft;
  }
  public BufferedImage getLifeImage()
  {
    return life;
  }
  public void updateLives()
  {
    BufferedImage lifeImg = null;
    try
    {
       lifeImg = ImageIO.read(new File(imgFileLife));
    } catch (IOException e) {}

    life = new BufferedImage(160, 24, BufferedImage.TYPE_INT_ARGB);

    Graphics2D lifeG = life.createGraphics();
    lifeG.drawImage(lifeImg, 0, 0, null);
  }
}
