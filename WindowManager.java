import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.HashMap;

/**
Manages all the panels
@author Kush Banker and Jack Basinet
@version 11.19.19
*/
public class WindowManager implements ActionListener
{
  private ScoreManager scoreManager;
  private JFrame window;
  private JPanel currentPanel;

  public WindowManager()
  {
    scoreManager = new ScoreManager();

    window = new JFrame("Space Invaders");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    currentPanel = new MenuPanel(this);

    window.add(currentPanel);
    window.pack();
    window.setVisible(true);

    SoundHandler.playSong("sound/New-Hope.wav");
  }

  public void updatePanel(JPanel panel)
  {
    window.remove(currentPanel);

    currentPanel = panel;

    window.add(currentPanel);
    window.pack();
    window.setVisible(true);
  }

  public void saveScore(String name, int score)
  {
    try
    {
      scoreManager.addScore(name, score);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public HashMap<Integer, String> getHighScores()
  {
    return scoreManager.getScores();
  }

  public void setWindowCursor()
  {
    // create new blank cursor
    BufferedImage cursorImg = null;

    BufferedImage cursorImgFile = null;
    try
    {
       cursorImgFile = ImageIO.read(new File("img/pointer1.jpg"));
    } catch (IOException e) {}

    cursorImg = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
    Graphics2D mouseG = cursorImg.createGraphics();
    mouseG.drawImage(cursorImgFile, 0, 0, null);

    Cursor targetCursor = Toolkit.getDefaultToolkit().createCustomCursor( cursorImg, new Point(0, 0), "targeter cursor");

    // set cursor to window
    window.getContentPane().setCursor(targetCursor);
  }

  public void resetWindowCursor()
  {
    window.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }

  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();

    if(command.equals("startGame"))
    {
      this.updatePanel(new SpaceInvaders(this, window));
      this.setWindowCursor();
      SoundHandler.playSong("sound/start.wav");
    }
    else if(command.equals("helpScreen"))
    {
      this.updatePanel(new HelpPanel(this));
      this.resetWindowCursor();
    }
    else if(command.equals("menuScreen"))
    {
      if(currentPanel instanceof EndPanel) { SoundHandler.playSong("sound/New-Hope.wav"); }

      this.updatePanel(new MenuPanel(this));
      this.resetWindowCursor();
    }
    else if(command.equals("mute"))
    {
      SoundHandler.mute();
      if(currentPanel instanceof MenuPanel)
      {
        ((MenuPanel) currentPanel).toggleMuteImage();
      }
    }
    else if(command.equals("info"))
    {
      this.updatePanel(new InfoPanel(this));
      this.resetWindowCursor();
    }
  }

  /**
  Stores and retrieves the scores
  @author Kush Banker and Jack Basinet
  @version 11.20.19
  */
  class ScoreManager
  {
    public static final String FILE_NAME = "scores.ser";

    private HashMap<Integer, String> scores;

    public ScoreManager()
    {
      scores = new HashMap<Integer, String>();

      try
      {
        scores = this.readScores();
      } catch(Exception e) {}
    }

    public void addScore(String name, int score) throws IOException
    {
      FileOutputStream fos = new FileOutputStream(FILE_NAME, false);
      ObjectOutputStream oos = new ObjectOutputStream(fos);

      scores.put(score, name);

      oos.writeObject(scores);
      fos.close();
    }

    public HashMap<Integer, String> getScores()
    {
      return scores;
    }

    //To convert the object that was read to a hashmap
    @SuppressWarnings("unchecked")
    public HashMap<Integer, String> readScores() throws IOException, ClassNotFoundException
    {
      FileInputStream fis = new FileInputStream(FILE_NAME);
      ObjectInputStream ois = new ObjectInputStream(fis);

      scores = (HashMap<Integer, String>) ois.readObject();

      ois.close();
      return scores;
    }

  }
}
