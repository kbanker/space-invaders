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

  public static final int MINI_SPAWN_RATE_WAVE_MS = 300;

  private int alien_spawn_rate_ms;
  private int twin_spawn_rate_ms;
  private int cannon_spawn_rate_ms;
  private int mini_spawn_rate_ms;
  private int hearty_spawn_rate_ms;
  private int spike_spawn_rate_ms;

  private int minisPerWave;

  private int alienTimer;
  private int twinTimer;
  private int cannonTimer;
  private int miniTimer;
  private int heartyTimer;
  private int spikeTimer;

  private boolean miniWave;
  private int miniWaveCt;
  private int miniWaveX;

  private int timer;

  private ArrayList<Enemy> enemiesToSpawn;

  public Spawner()
  {
    alien_spawn_rate_ms = 2000;
    twin_spawn_rate_ms = 3000;
    cannon_spawn_rate_ms = 7000;
    mini_spawn_rate_ms = 9500;
    hearty_spawn_rate_ms = 10000;
    spike_spawn_rate_ms = 10000;

    timer = 0;

    alienTimer = 0;
    twinTimer = 0;
    cannonTimer = 0;
    miniTimer = 0;
    heartyTimer = 0;
    spikeTimer = 0;

    enemiesToSpawn = new ArrayList<Enemy>();
  }

  /**
  THE MOST IMPORTANT METHOD OF THEM ALL
  @param ms Milliseconds
  */
  public void tick(int ms)
  {
    timer += ms;

    alienTimer += ms;
    if(timer >= 25000) { twinTimer += ms; }
    if(timer >= 55000) { cannonTimer += ms; }
    if(timer >= 60000)
    {
      int ifHeart = (int)(Math.random()*3);
      if(ifHeart == 2) { heartyTimer += ms; }
    }
    if(timer >= 90000)
    {
      miniTimer += ms;
      alien_spawn_rate_ms = 2800;
      twin_spawn_rate_ms = 3200;
    }
    if(timer >= 130000) { spikeTimer += ms; }
    //if(timer >= 150000) { tankTimer += ms; }
    if(timer == 180000) { this.harder(); }
    if(timer == 220000) { this.harder(); }
    if(timer == 240000) { this.harder(); }

    //System.out.println(timer);
  }

  public void harder()
  {
    alien_spawn_rate_ms -= 200;
    twin_spawn_rate_ms -= 200;
    cannon_spawn_rate_ms -= 200;
    mini_spawn_rate_ms -= 200;
    hearty_spawn_rate_ms -= 200;
  }

  public ArrayList<Enemy> spawn()
  {
    enemiesToSpawn = new ArrayList<Enemy>();
    //Alien
    if(alienTimer >= alien_spawn_rate_ms)
    {
      enemiesToSpawn.add(new Alien((int) (Math.random()*(WINDOW_WIDTH-120) + 60), 0));
      alienTimer = 0;
    }
    // Twin
    if(twinTimer >= twin_spawn_rate_ms)
    {
      enemiesToSpawn.add(new Twin((int) (Math.random()*(WINDOW_WIDTH-120) + 60), 0));
      twinTimer = 0;
    }
    // Cannon
    if(cannonTimer >= cannon_spawn_rate_ms)
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
    else if(miniTimer >= mini_spawn_rate_ms)
    {
      miniTimer = 0;
      miniWave = true;
      miniWaveCt = 0;
      minisPerWave = (int) (Math.random() * 4 + 2);

      miniWaveX = (int) (Math.random()*(WINDOW_WIDTH-120) + 60);
    }
    //HEARTY
    if(heartyTimer >= hearty_spawn_rate_ms)
    {
      enemiesToSpawn.add(new Hearty((int) (Math.random()*(WINDOW_WIDTH-120) + 60), 0));
      heartyTimer = 0;
    }
    //Spike
    if(spikeTimer >= spike_spawn_rate_ms)
    {
      enemiesToSpawn.add(new Spike((int) (Math.random()*(WINDOW_WIDTH-120) + 60), 0));
      spikeTimer = 0;
    }

    return enemiesToSpawn;
  }
}
