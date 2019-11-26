import java.util.ArrayList;

/**
Handles all spawning
@author Kush Banker and Jack Basinet
@version 11.18.19
*/
class Spawner
{
  public static final int WINDOW_WIDTH = 800;
  public static final int WINDOW_HEIGHT = 600;

  public static final int ALIEN_SPAWN_RATE_MS = 2000;
  public static final int TWIN_SPAWN_RATE_MS = 3000;
  public static final int CANNON_SPAWN_RATE_MS = 7000;
  public static final int MINI_SPAWN_RATE_MS = 9000;

  public static final int MINI_SPAWN_RATE_WAVE_MS = 300;

  private int minisPerWave;

  private int alienTimer;
  private int twinTimer;
  private int cannonTimer;
  private int miniTimer;

  private boolean miniWave;
  private int miniWaveCt;
  private int miniWaveX;

  private int timer;

  private ArrayList<Enemy> enemiesToSpawn;

  public Spawner()
  {
    timer = 0;

    alienTimer = 0;
    twinTimer = 0;
    cannonTimer = 0;
    miniTimer = 0;

    enemiesToSpawn = new ArrayList<Enemy>();
  }

  public void tick(int ms)
  {
    timer += ms;

    alienTimer += ms;
    if(timer >= 25000) { twinTimer += ms; }
    if(timer >= 55000) {   cannonTimer += ms; }
    if(timer >= 90000) { miniTimer += ms; }
    //if(timer >= 130000) { spikeTimer += ms; }
    //if(timer >= 180000) { tankTimer += ms; }
  }

  public ArrayList<Enemy> spawn()
  {
    enemiesToSpawn = new ArrayList<Enemy>();
    //Alien
    if(alienTimer >= ALIEN_SPAWN_RATE_MS)
    {
      enemiesToSpawn.add(new Alien((int) (Math.random()*(WINDOW_WIDTH-120) + 60), 0));
      alienTimer = 0;
    }
    // Twin
    if(twinTimer >= TWIN_SPAWN_RATE_MS)
    {
      enemiesToSpawn.add(new Twin((int) (Math.random()*(WINDOW_WIDTH-120) + 60), 0));
      twinTimer = 0;
    }
    // Cannon
    if(cannonTimer >= CANNON_SPAWN_RATE_MS)
    {
      enemiesToSpawn.add(new Cannon((int) (Math.random()*(WINDOW_WIDTH-120) + 60), 0));
      cannonTimer = 0;
    }
    // Mini
    if(miniWave)
    {
      if(miniWaveCt >= minisPerWave)
      {
        miniWave = false;
      }
      else if(miniTimer >= MINI_SPAWN_RATE_WAVE_MS)
      {
        enemiesToSpawn.add(new Mini(miniWaveX, 0));
        miniTimer = 0;
        miniWave = true;
        miniWaveCt++;
      }

    }
    else if(miniTimer >= MINI_SPAWN_RATE_MS)
    {
      miniTimer = 0;
      miniWave = true;
      miniWaveCt = 0;
      minisPerWave = (int) (Math.random() * 5 + 2);

      miniWaveX = (int) (Math.random()*(WINDOW_WIDTH-120) + 60);
    }

    return enemiesToSpawn;
  }
}
