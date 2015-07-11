import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;




public class ShipControls implements MouseListener, KeyListener {
  private RocketShip ship;
  
  
  
  
  public ShipControls (RocketShip newShip) {
    ship = newShip;
  }
  
  
  
  
  public final void mouseClicked(MouseEvent e) {}
  
  public final void mousePressed(MouseEvent e) {}
  
  public final void mouseReleased(MouseEvent e) {}
  
  public final void mouseExited(MouseEvent e) {}
  
  public final void mouseEntered(MouseEvent e) {}
  
  
  public final void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S)
      ship.adjustThrottle('y', Throttle.STALLED);
    else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D)
      ship.adjustThrottle('x', Throttle.STALLED);
  }
  
  
  public final void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_W)
      ship.adjustThrottle('y', Throttle.FORWARD);
    else if (e.getKeyCode() == KeyEvent.VK_S)
      ship.adjustThrottle('y', Throttle.BACKWARD);
    else if (e.getKeyCode() == KeyEvent.VK_D)
      ship.adjustThrottle('x', Throttle.FORWARD);
    else if (e.getKeyCode() == KeyEvent.VK_A)
      ship.adjustThrottle('x', Throttle.BACKWARD);
  }
  
  
  public final void keyTyped(KeyEvent e) {}
}