
import java.io.Serializable;

public class Queen extends Piece implements Serializable{
  
  public Queen(int x, int y, String color){
    super(x, y, color);    
  }
  
  public void printString(){
    super.toString();
  }
  
}