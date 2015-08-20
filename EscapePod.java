import java.awt.geom.*;
import java.awt.image.*;




public class EscapePod extends Body { // the goal at the end of each level
  public EscapePod(double newX, double newY, Space s) {
    super(newX, newY, 0, 0, (Math.random()-.5)*.001, 10, s);
  }
  
  
  
  @Override
  public final double getAX() {
    return 0;
  }
  
  
  @Override
  public final double getAY() {
    return 0;
  }
  
  
  @Override
  public final boolean canCollideWith(Body b) {
    return b.getClass().getName().equals("RocketShip");
  }
  
  
  @Override
  public final boolean collide() {
    return true;
  }
  
  
  @Override
  public final boolean update(double delT) {
    super.update(delT);
    if (Math.sqrt(Math.pow(getUniverse().getReference().getX()-this.getX(),2)+Math.pow(getUniverse().getReference().getY()-this.getY(),2)) < 300)
      getUniverse().triggerVictory();
    return false;
  }
  
  
  @Override
  public final String getFilePath() {
    return "textures/escapePod.png";
  }
}