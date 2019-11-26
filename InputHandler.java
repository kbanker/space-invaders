import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

/**
Handles all the key input
@author Kush Banker and Jack Basinet
@version 11.25.19
*/
class InputHandler implements KeyListener
{
  private Player player;

  private HashMap<String, Boolean> keyPressed;

  /**
  * Default SpaceKeyHandler constructor
  */
  public InputHandler(Player player)
  {
    super();
    this.player = player;
    this.keyPressed = new HashMap<String, Boolean>();

    keyPressed.put("space", false);
    keyPressed.put("c", false);
    keyPressed.put("left", false);
    keyPressed.put("right", false);
  }

  public void updatePlayer()
  {
    if (keyPressed.get("space")) { player.shoot(); }
    if (keyPressed.get("c")) { player.changeWeapon(); }
    if (keyPressed.get("left")) { player.move(false); }
    if (keyPressed.get("right")) { player.move(true); }
  }

  // Manage player movement and action
  public void keyPressed(KeyEvent e)
  {
     int keyCode = e.getKeyCode();

     if(keyCode == KeyEvent.VK_SPACE)
     {
       keyPressed.put("space", true);
     }
     else if(keyCode == KeyEvent.VK_C)
     {
       keyPressed.put("c", true);
     }
     else if(keyCode == KeyEvent.VK_LEFT)
     {

       keyPressed.put("left", true);
     }
     else if(keyCode == KeyEvent.VK_RIGHT)
     {
        keyPressed.put("right", true);
     }
     this.updatePlayer();
  }
  public void keyReleased(KeyEvent e)
  {
    int keyCode = e.getKeyCode();

    if(keyCode == KeyEvent.VK_SPACE)
    {
      keyPressed.put("space", false);
    }
    else if(keyCode == KeyEvent.VK_C)
    {
      keyPressed.put("c", false);
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
}
