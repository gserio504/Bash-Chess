import java.io.Serializable;

public class Rook extends Piece implements Serializable{  
  
  public Rook(int x, int y, String color){
    super(x, y, color);
    
  }
  
  public int getX(){
    return super.getX();
  }
  
  public int getY(){
    return super.getY();
  }
  
  public String getColor(){
    return super.getColor();
  }
  
  public void printString(){
    super.toString();
  }
  
}