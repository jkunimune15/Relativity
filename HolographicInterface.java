import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;




public class HolographicInterface { // a class to render and display tactical information (overhead view)
  public static final int WIDTH = 1280;
  public static final int HEIGHT = 800;
  private JFrame frame;
  private JPanel panel;
  private Canvas canvs;
  private BufferStrategy strat;
  private final Font serif;
  
  private Space universe;
  private BufferedImage background;
  private BufferedImage progressBar;
  private BufferedImage spedometer;
  private BufferedImage spedometerFrame;
  private BufferedImage[] cooldownBar;
  private BufferedImage[] healthBar;
  private BufferedImage[] lifeCounter;
  
  
  
  
  public HolographicInterface(Space wholeNewUniverse) {
    final ShipControls listener = new ShipControls(wholeNewUniverse, wholeNewUniverse.getReference());
    frame = new JFrame("Relativity");
    panel = new JPanel();
    canvs = new Canvas();
    serif = new Font("Serif", Font.ITALIC | Font.BOLD, 40);
    
    panel.setLayout(null);
    
    canvs.setBounds(0, 0, WIDTH, HEIGHT);
    canvs.setIgnoreRepaint(true);
    canvs.addKeyListener(listener);
    canvs.addMouseListener(listener);
    canvs.setFocusable(true); // Sets the canvas to focussable (Necessary for a key listener).
    
    panel.add(canvs);
    panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    
    frame.add(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(WIDTH, HEIGHT);
    frame.setVisible(true);
    frame.pack();
    
    canvs.createBufferStrategy(2);
    strat = canvs.getBufferStrategy();
    
    universe = wholeNewUniverse;
    
    loadImages();
  }
  
  
  
  
  public void display() {
    Graphics2D g = (Graphics2D) strat.getDrawGraphics();
    g.setFont(serif);
    g.setColor(Color.WHITE);
    
    g.clearRect(0, 0, WIDTH, HEIGHT); // Clears the screen
    g.drawImage(background, 0, 0, null); // Draws the background first and everything else on top of it
    
    for (Body b : universe) {
      g.drawImage(b.getSprite(), (int)b.getScreenX(), (int)b.getScreenY(), null); // Draws all of the enemies currently alive.
    }
    
    final double speedPerC = universe.getReference().getV()/Space.C; // the speed of the ship in % of lightspeed
    g.drawImage(progressBar, WIDTH-170, 20, WIDTH-20, 170, 0, 0, (int)(universe.getProgress()*150), 150, null);
    g.drawImage(spedometer, WIDTH-170, 20, WIDTH-170+(int)(speedPerC*150), 170, 0, 0, (int)(speedPerC*150), 150, null);
    g.drawImage(spedometerFrame, WIDTH-170, 20, null);
    g.drawImage(lifeCounter[universe.extraLives()], 20, 20, null);
    g.drawImage(healthBar[universe.getReference().getHP()], 20, 20, null);
    g.drawImage(cooldownBar[universe.getReference().laserCharge()], 20, 20, null);
    g.drawString(universe.getTime(), WIDTH-170, 60);
    
    g.dispose();
    strat.show(); // Shows the screen.
  }
  
  
  private void loadImages() {
    try {
      background = ImageIO.read(new File("textures/space.png"));
      progressBar = ImageIO.read(new File("textures/progressBar.png"));
      spedometer = ImageIO.read(new File("textures/spedometer.png"));
      spedometerFrame = ImageIO.read(new File("textures/spedometerFrame.png"));
      
      cooldownBar = new BufferedImage[9];
      for (int i = 0; i < cooldownBar.length; i ++)
        cooldownBar[i] = ImageIO.read(new File("textures/cooldownBar"+i+".png"));
      
      healthBar = new BufferedImage[17];
      for (int i = 0; i < healthBar.length; i ++)
        healthBar[i] = ImageIO.read(new File("textures/healthBar"+i+".png"));
      
      lifeCounter = new BufferedImage[3];
      for (int i = 0; i < lifeCounter.length; i ++)
        lifeCounter[i] = ImageIO.read(new File("textures/lifeCounter"+i+".png"));
    }
    catch (IOException e) {
      System.err.println(e);
    }
  }
}