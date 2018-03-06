import java.io.Serializable;

public class Bishop extends Piece implements Serializable{
  
  public Bishop(int x, int y, String color){
    super(x, y, color);  
  }
  
  public void printString(){
    super.toString();
  }
  
}