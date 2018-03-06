import java.io.Serializable;

public class Pawn extends Piece implements Serializable{  
  
  public Pawn(int x, int y, String color){
    super(x, y, color);    
  }
  
  public void printString(){
    super.toString();
  }
}