import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

/**
the sound handler
@author Jack Basinet and Kush Banker
@version 11.25.19
*/
class SoundHandler
{
  private static Clip clip;

  public static void playSong(String song)
  {
    try {
      if (clip != null) { clip.stop(); }
         File soundFile = new File(song);
         AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
         clip = AudioSystem.getClip();

         clip.open(audioIn);
         clip.start();
         clip.loop(Clip.LOOP_CONTINUOUSLY);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
  }

  public static void playSound(String sound)
  {
    try {
         File soundFile = new File(sound);
         AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
         Clip clip = AudioSystem.getClip();
         clip.open(audioIn);
         clip.start();
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
  }
}
