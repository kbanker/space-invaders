import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
Space Invaders
@author Kush Banker and Jack Basinet
@version 11.14.19
*/
public class SpaceInvaders extends JPanel implements ActionListener
{
   // Constants
   public static final int WINDOW_WIDTH = 800;
   public static final int WINDOW_HEIGHT = 600;

   public static final int TICK_MS = 40;

   // Instance variables

   private WindowManager windowManager;
   private InputHandler inputHandler;

   private Player player;

   private Spawner spawner;

   // Milliseconds since game started
   private int timeElapsed;

   private Timer timer;

   private boolean gameRunning;

   // Arrays of Enemies
   private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

   // Image for background
   private BufferedImage background = null;

   // Constructor for the SpaceInvaders class
   public SpaceInvaders(WindowManager listener, JFrame window)
   {
      //Timer that ticks every 40 ms
      timer = new Timer(TICK_MS, this);
      timer.start();

      this.player = new Player();
      inputHandler = new InputHandler(player, timer);

      //Allows for Key Listening
      window.addKeyListener(inputHandler);
      window.setFocusable(true);
      window.requestFocusInWindow();

      //Initialize variables
      windowManager = listener;
      this.initializeVariables();

      this.addMouseMotionListener(inputHandler);
      this.addMouseListener(inputHandler);
   }

   // Initializes variables
   public void initializeVariables()
   {
      this.timeElapsed = 0;
      this.gameRunning = true;

      //this.player = new Player();
      this.spawner = new Spawner();

      //Load background image
      BufferedImage backgroundImg = null;
      try
      {
         backgroundImg = ImageIO.read(new File("img/Background.png"));
      } catch (IOException e) {}

      background = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);

      Graphics2D backgroundG = background.createGraphics();
      backgroundG.drawImage(backgroundImg, 0, 0, null);
   }

   // Update main entities
   public void updatePlayer(Graphics g)
   {
     player.updatePlayer();

     g.drawImage(player.getImage(), player.getX(), player.getY(), null);

     if(player.getLives() <= -8) { gameRunning = false; }

     if(timeElapsed == 2000) { SoundHandler.playSong("sound/Boss-Fight.wav"); }
   }
   public void updateEnemies(Graphics g)
   {
     Graphics2D g2 = (Graphics2D) g;
     spawner.tick(TICK_MS);

     for(Enemy en: spawner.spawn()) { enemies.add(en); }

     ArrayList<Enemy> enemiesRemo = new ArrayList<Enemy>();

     for(Enemy en: enemies)
     {
       en.updateEnemy(TICK_MS);

       g.drawImage(en.getImage(), en.getX(), en.getY(), null);

       // Removing enemies
       if(en.getHealth() <= 0) { enemiesRemo.add(en); }
       if(en.getY() >= WINDOW_HEIGHT)
       {
         player.hurt(1);
         enemiesRemo.add(en);
       }
       if(g2.hit(en.getBounds(), player.getBounds(), false))
       {
         if(player.getGun() != 4) { player.hurt(en.getMeleeDamage()); }
         enemiesRemo.add(en);
       }
     }
     for(Enemy en: enemiesRemo)
     {
       if(en instanceof Alien) { player.score(1); }
       else if(en instanceof Twin) { player.score(2); }
       else if(en instanceof Cannon) { player.score(3); }
       else if(en instanceof Mini) { player.score(2); }

       enemies.remove(en);
       SoundHandler.playSound("sound/endie.wav");
     }
   }

   // Update bullets and hits
   public void updatePlayerBullets(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) g;

      ArrayList<PlayerBullet> bulletsRemo = new ArrayList<PlayerBullet>();

      for(PlayerBullet b: player.getBullets())
      {
         if (b.getY() < 0)
         {
            bulletsRemo.add(b);
         }
         else
         {
            int size = b.getDiam();

            g2.setColor(Color.GREEN);
            Ellipse2D.Double bulletGraphic = new Ellipse2D.Double(b.getX(), b.getY(), size, size);
            g2.fill(bulletGraphic);

            Rectangle boundingBox = new Rectangle(b.getX(), b.getY(), size, size);

            for(Enemy en: enemies)
            {
              Rectangle ship = en.getBounds();
              if(g2.hit(boundingBox, ship, false))
              {
                 bulletsRemo.add(b);

                 en.hurt(b.getDamage());
              }
            }
            b.updateBullet();
         }
      }
      for (PlayerBullet b: bulletsRemo) {player.removeBullet(b);}
   }
   public void updateEnemyBullets(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) g;

      for(Enemy en: enemies)
      {
        ArrayList<EnemyBullet> bulletsRemo = new ArrayList<EnemyBullet>();

        for(EnemyBullet b: en.getBullets())
        {
          if (b.getY() > WINDOW_HEIGHT)
          {
             bulletsRemo.add(b);
          }
          else
          {
             int size = b.getDiam();

             g2.setColor(Color.RED);
             Ellipse2D.Double bulletGraphic = new Ellipse2D.Double(b.getX(), b.getY(), b.getDiam(), b.getDiam());
             g2.fill(bulletGraphic);

             Rectangle enBulletBounds = new Rectangle(b.getX(), b.getY(), size, size);

             //Player collisions
             Rectangle playerBounds = player.getBounds();

             if(g2.hit(enBulletBounds, playerBounds, false))
             {
                bulletsRemo.add(b);

                player.hurt(b.getDamage());
             }
             b.updateBullet();
          }
        }
        for(EnemyBullet b: bulletsRemo){ en.removeBullet(b);}
      }
    }

   // Manage scores, background, and ending
   public void updateInGameCounters(Graphics g)
   {
      int scoreHeight;
      g.setColor(Color.WHITE);

      if(player.getScore() > 100)
      {
        g.drawString("Key 'C' : change Weapon", WINDOW_WIDTH - 162, WINDOW_HEIGHT - 18);
        scoreHeight = WINDOW_HEIGHT-38;
      }
      else { scoreHeight = WINDOW_HEIGHT-30; }

      g.drawString("Score: " + player.getScore(), WINDOW_WIDTH-80, scoreHeight);
      g.drawImage(player.getLifeImage(), 20, WINDOW_HEIGHT-45, null);
   }
   public void setBackground(Graphics g)
   {
      g.drawImage(background, 0, 0, null);
   }


   // Sets the dimensions of the window
   @Override
   public Dimension getPreferredSize()
   {
      return new Dimension(SpaceInvaders.WINDOW_WIDTH, SpaceInvaders.WINDOW_HEIGHT);
   }

   // This method is called everytime the repaint() method is called to paint the contents of the window
   @Override
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      this.setBackground(g);

      this.updatePlayerBullets(g);
      this.updateEnemyBullets(g);

      this.updatePlayer(g);
      this.updateEnemies(g);

      this.updateInGameCounters(g);
   }

   // Code that is performed every time the timer ticks
   public void actionPerformed(ActionEvent e)
   {
      this.repaint();
      if(gameRunning){
        timeElapsed += TICK_MS;
        inputHandler.updatePlayer();
      }
      else
      {
        SoundHandler.playSound("sound/death.wav");

        timer.stop();
        windowManager.updatePanel(new EndPanel(windowManager, player.getScore(), (int) (timeElapsed / 1000)));
        windowManager.resetWindowCursor();
      }
   }
}
