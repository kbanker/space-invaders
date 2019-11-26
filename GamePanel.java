import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.NavigableSet;

/**
Abstract class for panels
@author
@version
*/
abstract class GamePanel extends JPanel
{
  public static final int WINDOW_WIDTH = 800;
  public static final int WINDOW_HEIGHT = 600;

  protected BufferedImage backgroundImg;
  protected String imgFileName;
  protected WindowManager windowManager;

  public GamePanel(WindowManager listener)
  {
    windowManager = listener;

    this.setLayout(null);
  }
  public void loadImg()
  {
    try
    {
       backgroundImg = ImageIO.read(new File(imgFileName));
    } catch (IOException ex) {}
  }

  // Sets the dimensions of the window
  @Override
  public Dimension getPreferredSize()
  {
     return new Dimension(GamePanel.WINDOW_WIDTH, GamePanel.WINDOW_HEIGHT);
  }
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(backgroundImg, 0, 0, null);
  }
}


/**
Displays the Help Screen
@author Kush Banker and Jack Basinet
@version 11.19.19
*/
class HelpPanel extends GamePanel
{

  private JButton startButton;
  private JButton menuButton;

  public HelpPanel(WindowManager listener)
  {
    super(listener);

    menuButton = new JButton(new ImageIcon("img/menu.png"));
    menuButton.setBounds(605, 20, 175, 65);

    this.add(menuButton);

    menuButton.setActionCommand("menuScreen");
    menuButton.addActionListener(listener);

    imgFileName = "img/help.png";
    this.loadImg();
  }
}

/**
Displays the Menu
@author Kush Banker and Jack Basinet
@version 11.19.19
*/
class MenuPanel extends GamePanel
{
  private JButton startButton;
  private JButton helpButton;
  private JButton muteButton;

  public MenuPanel(WindowManager listener)
  {
    super(listener);

    startButton = new JButton(new ImageIcon("img/start.png"));
    startButton.setBounds(45, 260, 195, 60);

    this.add(startButton);

    startButton.setActionCommand("startGame");
    startButton.addActionListener(listener);

    helpButton = new JButton(new ImageIcon("img/helpButton.png"));
    helpButton.setBounds(45, 330, 190, 60);

    this.add(helpButton);

    helpButton.setActionCommand("helpScreen");
    helpButton.addActionListener(listener);

    muteButton = new JButton(new ImageIcon("img/sound.png"));
    muteButton.setBounds(20, 20, 60, 60);

    this.add(muteButton);

    muteButton.setActionCommand("mute");
    muteButton.addActionListener(listener);

    imgFileName = "img/StartScreen.png";
    this.loadImg();
  }

  public void toggleMuteImage()
  {
    if(SoundHandler.mute) { muteButton.setIcon(new ImageIcon("img/mute.png")); }
    else { muteButton.setIcon(new ImageIcon("img/sound.png")); }
  }
}

/**
Displays the End Screen
@author Kush Banker and Jack Basinet
@version 11.19.19
*/
class EndPanel extends GamePanel implements ActionListener
{
  private String name;
  private int score;
  private int seconds;

  private JButton startButton;
  private JButton menuButton;

  private JTextField nameTextField;
  private JTextArea scoreDisplayTextArea;
  private JScrollPane scoreDisplayPane;

  public EndPanel(WindowManager listener, int score, int seconds)
  {
    super(listener);

    this.score = score;
    this.seconds = seconds;

    //Restart button
    startButton = new JButton(new ImageIcon("img/restart.png"));
    startButton.setBounds(20, 20, 210, 60);
    this.add(startButton);
    startButton.setActionCommand("startGame");
    startButton.addActionListener(listener);

    //menu button
    menuButton = new JButton(new ImageIcon("img/menu.png"));
    menuButton.setBounds(20, 100, 175, 60);
    this.add(menuButton);
    menuButton.setActionCommand("menuScreen");
    menuButton.addActionListener(listener);

    //Enter name text box
    nameTextField = new JTextField(20);
    nameTextField.setBounds(400, 310+83, 100, 30);
    this.add(nameTextField);
    nameTextField.setActionCommand("nameEntered");
    nameTextField.addActionListener(this);

    //High Score Display
    scoreDisplayTextArea = new JTextArea();
    scoreDisplayTextArea.setEditable(false);

    scoreDisplayPane = new JScrollPane(scoreDisplayTextArea);
    scoreDisplayPane.setBounds(600, 320, 180, 200);
    this.add(scoreDisplayPane);

    scoreDisplayTextArea.setBackground(Color.BLACK);
    scoreDisplayTextArea.setForeground(Color.WHITE);
    scoreDisplayTextArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

    this.updateScoreDisplay();

    imgFileName = "img/end.png";
    this.loadImg();
  }

  public void updateScoreDisplay()
  {
    HashMap<Integer, String> scoresAndNames = windowManager.getHighScores();
    NavigableSet<Integer> scores = (new TreeSet<Integer>(scoresAndNames.keySet())).descendingSet();

    scoreDisplayTextArea.setText("");

    for(Integer score: scores)
    {
      scoreDisplayTextArea.append(scoresAndNames.get(score) + ": " + score + "\n");
    }

  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    //Score and time
    g.setColor(Color.WHITE);
    g.drawString("Name: ", 350, 330+83);
    g.drawString("You scored: " + score + " pts", 400, 443);
    g.drawString("You survived for " + seconds + " seconds", 400, 463);
  }

  public void actionPerformed(ActionEvent e)
  {
    if(e.getActionCommand().equals("nameEntered"))
    {
      name = nameTextField.getText();
      nameTextField.setText("Done!");
      nameTextField.setEditable(false);

      windowManager.saveScore(name, score);

      this.updateScoreDisplay();
    }
  }

}
