import java.io.Serializable;

public class King extends Piece implements Serializable{  
  
  public King(int x, int y, String color){
    super(x, y, color);
  }
  
  public void printString(){
    super.toString();
  }
  
}