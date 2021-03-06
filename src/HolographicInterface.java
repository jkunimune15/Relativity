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
  private BufferedImage[] background;
  private BufferedImage progressBar;
  private BufferedImage spedometer;
  private BufferedImage spedometerFrame;
  private BufferedImage[] cooldownBar;
  private BufferedImage[] healthBar;
  private BufferedImage[] lifeCounter;
  private BufferedImage startBut, exitBut, scoresBut, helpBut;
  
  
  
  
  public HolographicInterface(Space wholeNewUniverse) {
    universe = wholeNewUniverse;
    frame = new JFrame("Relativity");
    panel = new JPanel();  
    canvs = new Canvas();
    serif = new Font("Serif", Font.ITALIC | Font.BOLD, 40);
    
    final ShipControls listener = new ShipControls(universe, frame);
    canvs.addKeyListener(listener);
    canvs.addMouseListener(listener);
    
    panel.setLayout(null);
    
    canvs.setBounds(0, 0, WIDTH, HEIGHT);
    canvs.setIgnoreRepaint(true);
    canvs.setFocusable(true); // Sets the canvas to focusable (Necessary for a key listener).
    
    panel.add(canvs);
    panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    panel.setOpaque(true);
    
    frame.setContentPane(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(WIDTH, HEIGHT);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
    
    canvs.createBufferStrategy(2);
    strat = canvs.getBufferStrategy();
    
    loadImages();
  }
  
  
  
  
  public void display() {
    Graphics2D g = (Graphics2D) strat.getDrawGraphics();
    g.setFont(serif);
    g.setColor(Color.WHITE);
    
    g.clearRect(0, 0, WIDTH, HEIGHT); // Clears the screen
    
    final Point ship = new Point((int)universe.getReference().getX(), (int)universe.getReference().getY());
    g.drawImage(background[0],                              0,                              0, null);
    for (int i = 1; i < background.length; i ++) {
      g.drawImage(background[i],      -mod(i*i*ship.x>>7,WIDTH),       -mod(i*i*ship.y>>7,HEIGHT), null); // Draws the background first and everything else on top of it
      g.drawImage(background[i], WIDTH-mod(i*i*ship.x>>7,WIDTH),       -mod(i*i*ship.y>>7,HEIGHT), null); // Draws 4 so that it can pan continuously
      g.drawImage(background[i],      -mod(i*i*ship.x>>7,WIDTH), HEIGHT-mod(i*i*ship.y>>7,HEIGHT), null);
      g.drawImage(background[i], WIDTH-mod(i*i*ship.x>>7,WIDTH), HEIGHT-mod(i*i*ship.y>>7,HEIGHT), null);
    }
    
    for (Body b : universe) {
      final Point coord = b.getScreenCoords();
      g.drawImage(b.getSprite(), coord.x, coord.y, null); // Draws all of the things
    }
    
    switch (universe.gameState) { // changes the HUD based on gamemode
      case DISPLAY:
        g.drawImage(startBut, WIDTH/2 - startBut.getWidth()/2, HEIGHT/2-200 - startBut.getHeight()/2, null);
        g.drawImage(exitBut, WIDTH/2 - exitBut.getWidth()/2, HEIGHT/2+200 - exitBut.getHeight()/2, null);
        g.drawImage(scoresBut, WIDTH/2+346 - scoresBut.getWidth()/2, HEIGHT/2 - scoresBut.getHeight()/2, null);
        g.drawImage(helpBut, WIDTH/2-346 - helpBut.getWidth()/2, HEIGHT/2 - helpBut.getHeight()/2, null);
        break;
      case VICTORIOUS:
      case DEAD:
      case RUNNING:
        final double speedPerC = universe.getReference().getV()/Space.C; // the speed of the ship in % of lightspeed
        g.drawImage(progressBar, WIDTH-170, 20, WIDTH-20, 170, 0, 0, (int)(universe.getProgress()*150), 150, null); //draws the entire HUD
        g.drawImage(spedometer, WIDTH-170, 20, WIDTH-170+(int)(speedPerC*150), 170, 0, 0, (int)(speedPerC*150), 150, null);
        g.drawImage(spedometerFrame, WIDTH-170, 20, null);
        g.drawImage(lifeCounter[universe.extraLives()], 20, 20, null);
        g.drawImage(healthBar[universe.getReference().getHP()], 20, 20, null);
        g.drawImage(cooldownBar[universe.getReference().laserCharge()], 20, 20, null);
        g.drawString(universe.getTime(), WIDTH-170, 60);
      case DONE:
    	break;
    }
    
    g.dispose();
    strat.show(); // Shows the screen.
  }
  
  
  private int mod(int x, int y) { // a method I created to be better than %
    int a = x;
    while (a >= y)
      a -= y;
    while (a < 0)
      a += y;
    return a;
  }
  
  
  public void setSpace(Space spaaaaace) {
    universe = spaaaaace;
    final ShipControls listener = new ShipControls(spaaaaace, frame);
    canvs.addKeyListener(listener);
    canvs.addMouseListener(listener);
  }
  
  
  private void loadImages() {
    try {
      background = new BufferedImage[4];
      for (int i = 0; i < background.length; i ++)
        background[i] = ImageIO.read(new File("textures/background"+i+".png"));
      
      startBut = ImageIO.read(new File("textures/button.START.png"));
      exitBut = ImageIO.read(new File("textures/button.EXIT.png"));
      scoresBut = ImageIO.read(new File("textures/button.SCORES.png"));
      helpBut = ImageIO.read(new File("textures/button.HELP.png"));
      
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