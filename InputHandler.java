import java.awt.event.*;
import java.awt.*;

/**
Handles all the key input
@author Kush Banker and Jack Basinet
@version 11.25.19
*/
class InputHandler implements KeyListener
{
  private Player player;

  /**
  * Default SpaceKeyHandler constructor
  */
  public SpaceKeyHandler(Player player)
  {
    super();
    this.player = player;
  }

  // Manage player movement and action
  public void keyPressed(KeyEvent e)
  {
     int keyCode = e.getKeyCode();

     if(keyCode == KeyEvent.VK_SPACE) { player.shoot(); }
     else if(keyCode == KeyEvent.VK_C) { player.changeWeapon(); }
     else if(keyCode == KeyEvent.VK_LEFT) { player.move(false); }
     else if(keyCode == KeyEvent.VK_RIGHT) { player.move(true); }
  }
  public void keyReleased(KeyEvent e)
  {

  }
  public void keyTyped(KeyEvent e){}
}
