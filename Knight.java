import java.io.Serializable;

public class Knight extends Piece implements Serializable{
  
  public Knight(int x, int y, String color){
    super(x, y, color);
    
  }
  
  public void printString(){
    super.toString();
  }
  
}