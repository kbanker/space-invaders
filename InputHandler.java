import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import javax.swing.Timer;

/**
Handles all the key input
@author Kush Banker and Jack Basinet
@version 11.25.19
*/
class InputHandler implements KeyListener, MouseMotionListener, MouseListener
{
  private Player player;

  private HashMap<String, Boolean> keyPressed;

  private Timer timer;

  /**
  * Default SpaceKeyHandler constructor
  */
  public InputHandler(Player player, Timer timer)
  {
    super();
    this.player = player;
    this.keyPressed = new HashMap<String, Boolean>();
    this.timer = timer;

    keyPressed.put("space", false);
    keyPressed.put("up", false);
    keyPressed.put("left", false);
    keyPressed.put("right", false);
  }

  public void updatePlayer()
  {
    if (keyPressed.get("space") || keyPressed.get("up")) { player.shoot(); }
    if (keyPressed.get("left")) { player.move(false); }
    if (keyPressed.get("right")) { player.move(true); }
  }

  // Manage player movement and action
  public void keyPressed(KeyEvent e)
  {
     int keyCode = e.getKeyCode();

     if(keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_UP)
     {
       keyPressed.put("space", true);
       keyPressed.put("up", true);
     }
     else if(keyCode == KeyEvent.VK_C || keyCode == KeyEvent.VK_SLASH)
     {
       player.changeWeapon();
     }
     else if(keyCode == KeyEvent.VK_LEFT)
     {
       keyPressed.put("left", true);
     }
     else if(keyCode == KeyEvent.VK_RIGHT)
     {
        keyPressed.put("right", true);
     }
  }
  public void keyReleased(KeyEvent e)
  {
    int keyCode = e.getKeyCode();

    if(keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_UP)
    {
      keyPressed.put("space", false);
      keyPressed.put("up", false);
    }
    else if(keyCode == KeyEvent.VK_LEFT)
    {
      keyPressed.put("left", false);
    }
    else if(keyCode == KeyEvent.VK_RIGHT)
    {
       keyPressed.put("right", false);
    }
  }
  public void keyTyped(KeyEvent e){}

  public void mouseMoved(MouseEvent e)
  {
      if(player.getGun() == 4)
      {
        player.setY((int)((GamePanel.WINDOW_HEIGHT+e.getY())/(2.3)) - player.getHeight()/2);
        player.setX(e.getX() - player.getWidth()/2);
      }
      else
      {
        player.setX(e.getX() - player.getWidth()/2);
        player.setY(490);
      }
  }
  public void mouseDragged(MouseEvent e){}

  public void mouseReleased(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  public void mousePressed(MouseEvent e)
  {
    if(timer.isRunning()) {timer.stop(); }
    else { timer.start(); }
  }
}
