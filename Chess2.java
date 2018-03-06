import java.util.Scanner;
import java.io.Serializable;
public class Chess2 implements Serializable{
  
  private static Board board = new Board();
  private static transient Scanner input = new Scanner(System.in);
  private static VisualBoard gfx = new VisualBoard();
  private static boolean globalLooper = true;
  private static String choice = null;
  private static SaveFile filesaver;
  
 
  public static void main(String[] args){

    askToLoadSaveFile();
    System.out.println("\nType 'quit' at any time to stop the game");
    
    do{
      gfx.printBoard(board.getBoard(),board);
      choosePieceToMove();
      gameOver();
      stalemate();
    }
    while(globalLooper);
    
    gfx.printBoard(board.getBoard(),board);    
  }

  public static void askToLoadSaveFile(){
    filesaver = new SaveFile(board);
    filesaver.askToLoad();
    board = filesaver.getBoard();
    System.out.println(board.lastPieceToMove.getColor());
  }
  
  
  public static void choosePieceToMove(){
    boolean looper = true;
    int xCoord = -1;
    int yCoord = -1;
    int destX = -1;
    int destY = -1;
    while(looper){
      System.out.println("Type in move, example: 'a1-b1'");
      choice = input.next();
      if(choice.equals("quit")){
        globalLooper = false;
        looper = false;
      }
      else if(choice.equals("save")){
        filesaver.displaySaves();
        System.out.println("What would you like to name your save file?");
        filesaver.saveGame(input.next(),board);
      }
      else{
        if(choice.length()==5){//checks if any inputs left out as to not crash the program
          if(board.lastPieceToMove.getColor().equals("Black")){
            xCoord = convertLetterToNumber(choice.charAt(0));
            yCoord = Math.abs(Character.getNumericValue(choice.charAt(1))-8);
            destX = convertLetterToNumber(choice.charAt(3));
            destY = Math.abs(Character.getNumericValue(choice.charAt(4))-8);
          }
          else{
            xCoord = convertLetterToNumber(choice.charAt(0));
            yCoord = Math.abs(Character.getNumericValue(choice.charAt(1))-8);
            destX = convertLetterToNumber(choice.charAt(3));
            destY = Math.abs(Character.getNumericValue(choice.charAt(4))-8);
          }
          //System.out.println(xCoord + " " + yCoord+ " " + destX+ " " + destY);
          if(checkForProperInput(xCoord, yCoord, destX, destY))
            looper = false;
        }//if the string has a length of five
      }//executes else statement ife "quit" isnt typed in
    }//end while loop for taking in proper input
    if(!choice.equals("quit") && !choice.equals("save"))
      board.takeTurn(xCoord, yCoord, destX, destY,board.getBoard());
  }
  
  private static int convertLetterToNumber(char character){
    int number = 0;
    if((character=='a') || (character == 'A')){
      number = 0;
    }
    else if((character=='b') || (character == 'B')){
      number = 1;
    }
    else if((character=='c') || (character == 'C')){
      
      number = 2;
      
      
    }
    else if((character=='d') || (character == 'D')){
      
      number = 3;
      
    }
    else if((character=='e') || (character == 'E')){
      
      number = 4;
      
    }
    else if((character=='f') || (character == 'F')){
      
      number = 5;
      
    }
    else if((character=='g') || (character == 'G')){
      
      number = 6;
      
    }
    else if((character=='h') || (character == 'H')){
      
      number = 7;
      
    }
    else
      number = 5000;
    
    return number;
  }
  
  public static boolean checkForProperInput(int xCoord, int yCoord, int destX, int destY){
    boolean validInput = false;
    int four = 0;
    int [] numbers = {0,1,2,3,4,5,6,7};
    
    for(int j=0;j<numbers.length;j++){
      if(xCoord == numbers[j])
        four++;
      if(destX == numbers[j])
        four++;
      if(yCoord == numbers[j])
        four++;
      if(destY == numbers[j])
        four++;
    }
    
    if(four == 4)
      validInput = true;
    //System.out.println(four);
    return validInput;
    
  }
  
  public static boolean gameOver(){
    boolean gameOver = false;
    if(board.getCheckmate()){
      globalLooper = false;
      gameOver = true;
    }
    return gameOver;
  }
  
  public static boolean stalemate(){
    boolean stalemate = false;
    
    if(board.getStalemate()){
      globalLooper = false;
      stalemate = true;
      System.out.println("Dang, you tied.  It's a stalemate!");
    }
    return stalemate;
  }

  public static void setBoard(Board board){
    board = board;
  }
  
}