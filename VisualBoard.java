import java.io.Serializable;

public class VisualBoard implements Serializable{
  
  String[][] board = new String[8][8];
  
  
  public VisualBoard(){
  }
  //uses coordinates of pieces to draw it's image to the board.  Adds strings to String two-dimensional array
  private void drawBoard(){
    for(int i=0;i<board.length;i++){
      for(int j=0;j<board[i].length;j++){
        if((j%2==0) && (i%2==0))
          board[i][j] = /*"?" " # "; */ "   ";
        if((j%2!=0) && (i%2!=0))
          board[i][j] = /*" # "; */"   ";
        if((j%2==0) && (i%2!=0))
          board[i][j] = /*"   ";*/"...";
        if((j%2!=0) && (i%2==0))
          board[i][j] = /*"   ";*/"...";
        
        /*else
         board[i][j] = /*"?" " ";*/
      }
    }//end outer for loop
  }
  
  //prints the formatted contents of the two-dimensional string array
  public void printBoard(Piece[][] gameBoard, Board boardclass){
    drawBoard();
    fillBoardWithPieces(gameBoard, boardclass);
    if(boardclass.lastPieceToMove.getColor().equals("Black")){
      System.out.println("    A  B  C  D  E  F  G  H");
    }
    else
      System.out.println("    H  G  F  E  D  C  B  A");
    System.out.println("   ________________________");
    for(int i=0;i<board.length;i++){
      if(boardclass.lastPieceToMove.getColor().equals("Black")){
        System.out.print((Math.abs(i-8)) +" |");
      }
      else{
        System.out.print((i+1) +" |");
      }
      for(int j=0;j<board[i].length;j++){
        System.out.print(board[i][j]);
        if(j==7){
          if(boardclass.lastPieceToMove.getColor().equals("Black")){
            System.out.println("|" + (Math.abs(i-8)));
          }
          else{
            System.out.println("|" + (i+1));
          }
          if(i<7)
            System.out.println("  |                        |");
        }        
      }  
    }
    System.out.println("  --------------------------");
    if(boardclass.lastPieceToMove.getColor().equals("Black")){
      System.out.println("    A  B  C  D  E  F  G  H");
    }
    else
      System.out.println("    H  G  F  E  D  C  B  A");
  }//end printBoard
  
  private void fillBoardWithPieces(Piece[][] gameBoard, Board boardclass){
    if(boardclass.lastPieceToMove.getColor().equals("White"))
      gameBoard = invertGameBoard(gameBoard);
    
    for(int i=0; i<gameBoard.length; i++)
      for(int j=0; j<gameBoard[i].length; j++){
      checkForKing(gameBoard,i,j);
      checkForQueen(gameBoard,i,j);
      checkForRook(gameBoard, i, j);
      checkForBishop(gameBoard, i, j);
      checkForKnight(gameBoard,i,j);
      checkForPawn(gameBoard,i,j);
    }
  }
  
  private Piece[][] invertGameBoard(Piece[][] gameboard){
    Piece[][] tempboard = new Piece[8][8];
    
    for(int i=0; i<tempboard.length; i++)
      for(int j=0; j<tempboard[i].length; j++)
      tempboard[Math.abs(i-7)][Math.abs(j-7)] = gameboard[i][j];
    
    return tempboard;
  }
  
  
  //these draw the pieces based on what type of object they are
  private void checkForKing(Piece[][] gameBoard, int row, int column){
    if((gameBoard[row][column] instanceof King) && gameBoard[row][column].getColor().equals("White"))
      board[row][column] = "KNG";
    else if((gameBoard[row][column] instanceof King) && gameBoard[row][column].getColor().equals("Black"))
      board[row][column] = "kng";
  }
  
  private void checkForQueen(Piece[][] gameBoard, int row, int column){
    if((gameBoard[row][column] instanceof Queen) && gameBoard[row][column].getColor().equals("White"))
      board[row][column] = "*Q*";
    else if((gameBoard[row][column] instanceof Queen) && gameBoard[row][column].getColor().equals("Black"))
      board[row][column] = "*q*";
  }
  
  private void checkForRook(Piece[][] gameBoard, int row, int column){
    if((gameBoard[row][column] instanceof Rook) && gameBoard[row][column].getColor().equals("White"))
      board[row][column] = "*R*";
    else if((gameBoard[row][column] instanceof Rook) && gameBoard[row][column].getColor().equals("Black"))
      board[row][column] = " r ";
  }
  
  private void checkForBishop(Piece[][] gameBoard, int row, int column){
    if((gameBoard[row][column] instanceof Bishop) && gameBoard[row][column].getColor().equals("White"))
      board[row][column] = "*B*";
    else if((gameBoard[row][column] instanceof Bishop) && gameBoard[row][column].getColor().equals("Black"))
      board[row][column] = " b ";
  }
  
  private void checkForKnight(Piece[][] gameBoard, int row, int column){
    if((gameBoard[row][column] instanceof Knight) && gameBoard[row][column].getColor().equals("White"))
      board[row][column] = "*K*";
    else if((gameBoard[row][column] instanceof Knight) && gameBoard[row][column].getColor().equals("Black"))
      board[row][column] = " k ";
  }
  
  private void checkForPawn(Piece[][] gameBoard, int row, int column){
    if((gameBoard[row][column] instanceof Pawn) && gameBoard[row][column].getColor().equals("White"))
      board[row][column] = "*P*";
    else if((gameBoard[row][column] instanceof Pawn) && gameBoard[row][column].getColor().equals("Black"))
      board[row][column] = " p ";
  }
  
  
}