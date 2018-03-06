import java.io.Serializable;
import java.util.Scanner;

public class Board implements Serializable{
  
  
  Piece[][] board = new Piece[8][8];
  Piece[] pieces = new Piece[32];
  Piece lastPieceToMove = new Piece(-1,-1,"Black");
  transient Scanner input = new Scanner(System.in);
  boolean checkmate = false;
  boolean stalemate = false;
  
  
  
  public Board(){
    fillPieceArray();
    updateBoard();
  }
  
  public boolean getCheckmate(){
    return checkmate;
  }
  
  private void fillPieceArray(){
    //list of black pieces
    
    pieces[0] = new King(4,0,"Black");
    pieces[1] = new Queen(3,0,"Black");
    
    pieces[2] = new Rook(0,0,"Black");
    pieces[3] = new Rook(7,0,"Black");
    
    pieces[4] = new Bishop(2,0,"Black");
    pieces[5] = new Bishop(5,0,"Black");
    
    pieces[6] = new Knight(1,0,"Black");
    pieces[7] = new Knight(6,0,"Black");
    
    pieces[8] = new Pawn(0,1,"Black");
    pieces[9] = new Pawn(1,1,"Black");
    pieces[10] = new Pawn(2,1,"Black");
    pieces[11] = new Pawn(3,1,"Black");
    pieces[12] = new Pawn(4,1,"Black");
    pieces[13] = new Pawn(5,1,"Black");
    pieces[14] = new Pawn(6,1,"Black");
    pieces[15] = new Pawn(7,1,"Black"); 
    
    //list of white pieces
    
    pieces[16] = new King(4,7,"White");
    pieces[17] = new Queen(3,7,"White");
    
    pieces[18] = new Rook(0,7, "White");
    pieces[19] = new Rook(7,7,"White");
    
    pieces[20] = new Bishop(2,7,"White");
    pieces[21] = new Bishop(5,7,"White");
    
    pieces[22] = new Knight(1,7,"White");
    pieces[23] = new Knight(6,7,"White");
    
    pieces[24] = new Pawn(0,6,"White");
    pieces[25] = new Pawn(1,6,"White");
    pieces[26] = new Pawn(2,6,"White");
    pieces[27] = new Pawn(3,6,"White");
    pieces[28] = new Pawn(4,6,"White");
    pieces[29] = new Pawn(5,6,"White");
    pieces[30] = new Pawn(6,6,"White");
    pieces[31] = new Pawn(7,6,"White");
  }
  
  //relocates pieces based on their xy value
  private void updateBoard(){      
    
    //clear board
    for(int a=0; a<board.length; a++)
      for(int b=0; b<board[a].length; b++)
      board[a][b] = null;
    
    //refills board with pieces from piece array
    for(int i=0; i<pieces.length; i++){
      for(int j=0; j<board.length; j++)
        for(int k=0; k<board[j].length; k++)      
        if((j==pieces[i].getY() && k==pieces[i].getX()))
        board[j][k] = pieces[i];
    }
  }
  
  public Piece[][] getBoard(){
    return board;
  }
  
  //checks what piece is at that spot in the board, downcasts it to call specified piece's move method
  public void takeTurn(int x, int y, int destX, int destY, Piece[][] board){
    
    if(alternateTurns(x,y)==true){
      
      for(int i=0; i<pieces.length;i++)
        if(pieces[i].getX()==x && pieces[i].getY()==y){          
        if(pieces[i].movePiece(destX, destY, board, pieces)){
          lastPieceToMove = pieces[i];
          pieces[i].setLastPieceToMove(lastPieceToMove);
          for(int j=0;j<pieces.length; j++)//remove piece captured from board VVVV
            if(pieces[j].getX()==destX && pieces[j].getY()==destY && pieces[j].getColor()!=lastPieceToMove.getColor()){
            pieces[j].setX(-1);
            pieces[j].setY(-1);
          }
        }
        checkForCastle(pieces[i], destX, destY);//maybe relocate this at the top?
      }
    }
    checkIfPawnUpgrade();
    updateBoard();
    isKingInCheck(board);
    canAPieceMove();
  }
  
  //as the name implies
  private boolean alternateTurns(int x, int y){
    boolean moveValid = true;
    
    if(board[y][x] != null)
      if(lastPieceToMove.getColor()==board[y][x].getColor())
      moveValid = false;
    
    return moveValid;
  }
  
  
  private void checkIfPawnUpgrade(){
    //check pieces array for black pawn's y coordinates (if 7 then it reached the end)
    for(int i=8; i<15; i++)
      if(pieces[i].getY()==7 && pieces[i] instanceof Pawn)
      changePawn(i, pieces[i].getColor());
    //check white array(if 0 it has reached the end)  
    for(int j=24; j<31; j++)
      if(pieces[j].getY()==0 && pieces[j] instanceof Pawn)
      changePawn(j, pieces[j].getColor());
  }
  
  private void changePawn(int i, String color){
    String choice = null;
    boolean looper = true;
    while(looper){
      System.out.println("Upgrade to which piece?\n1)Rook\n2)Bishop\n3)Knight\n4)Queen");
      choice = input.next();
      if(choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4"))
        looper = false;
    }
    
    switch(choice){
      case "1":
        pieces[i] = new Rook(pieces[i].getX(), pieces[i].getY(), pieces[i].getColor());
        System.out.println("Here's your new rook!");
        break;
      case "2":
        pieces[i] = new Bishop(pieces[i].getX(), pieces[i].getY(), pieces[i].getColor());
        System.out.println("Here's your new bishop!");
        break;
      case "3":
        pieces[i] = new Knight(pieces[i].getX(), pieces[i].getY(), pieces[i].getColor());
        System.out.println("Here's your new knight!");
        break;
      case "4":
        pieces[i] = new Queen(pieces[i].getX(), pieces[i].getY(), pieces[i].getColor());
        System.out.println("Here's your new queen!");
        break;
        
    }
  }
  
  private boolean isKingInCheck(Piece[][] board){
    boolean kingIsInCheck = false;
    for(int i=17;i<32;i++)
      if(pieces[i].moveIsValid(pieces[0].getX(),pieces[0].getY(), board, pieces)){
      kingIsInCheck = true;
      pieces[0].setBlackKingInCheck();
      System.out.println("The Black King is in check!");
    }
    for(int j=1;j<15;j++)
      if(pieces[j].moveIsValid(pieces[16].getX(),pieces[16].getY(), board, pieces)){
      kingIsInCheck = true;
      System.out.println("The White King is in check!");
      pieces[16].setWhiteKingInCheck();
    } 
    
    isCheckmate();
    return kingIsInCheck;
  }
  
  
  private boolean canPieceMoveToKingsCoords(Piece king){
    boolean pieceCanMove = false;
    if(king.getColor().equals("White")){
      for(int i=1; i<15; i++) //maybe expand this to include other king?
        if(pieces[i].moveIsValid(king.getX(),king.getY(),board,pieces))
        pieceCanMove = true;
    }
    else if(king.getColor().equals("Black")){
      for(int i=17; i<32; i++)
        if(pieces[i].moveIsValid(king.getX(),king.getY(),board,pieces))
        pieceCanMove = true;
    }
    return pieceCanMove;
  }
  
  private void isCheckmate(){
    
    if(pieces[0].getBlackKingInCheck() || pieces[16].getWhiteKingInCheck()){
      if(lastPieceToMove.getColor().equals("White")){ //black in checkmate?
        if(!kingCanMove2()){
          System.out.println("White is truly the master race here");
          checkmate = true;
        }
      }//if last piece to move is was white
      else if(!kingCanMove2()){
        System.out.println("Somehow black defeated a more pure race");
        checkmate = true;
      }
    }//if a king is in check
  }
  
  private String pieceAtSpot(int x, int y){
    String myString = "";
    if(board[y][x]!=null)
      myString = board[y][x].getColor();
    return myString;
  }
  
  private void checkForCastle(Piece pieceMoved, int destX, int destY){
    if(pieceMoved instanceof King){
      if(pieceMoved.getColor().equals("White") && pieceMoved.hasntMoved()){
        
        if(!pieces[16].getWhiteKingInCheck()){
          //capturing to the right
          if((destX==6) && (destY==7)){
            if(board[7][5]==null && board[7][6]==null)
              if(pieces[19].hasntMoved()){
              if(!castleBlockedDownRight()){
                pieces[16].setY(7);
                pieces[16].setX(6);          
                pieces[19].setY(7);
                pieces[19].setX(5);
                lastPieceToMove = pieces[16];
              }
            }
          }
          else if((destX==2) && (destY==7)){
            if(board[7][3]==null && board[7][2]==null && board[7][1]==null)
              if(pieces[18].hasntMoved()){
              if(!castleBlockedDownLeft()){
                pieces[16].setY(7);
                pieces[16].setX(2);
                pieces[18].setY(7);
                pieces[18].setX(3);
                lastPieceToMove = pieces[16];
              }
            }
          }
        }
      }//end of checking if WHITE KING
      else if(pieceMoved.getColor().equals("Black") && pieceMoved.hasntMoved()){
        if(!pieces[0].getBlackKingInCheck()){
          if((destX==6) && (destY==0)){
            if(board[0][5]==null && board[0][6]==null)
              if(pieces[3].hasntMoved()){
              if(!castleBlockedUpRight()){
                pieces[0].setY(0);
                pieces[0].setX(6);
                pieces[3].setY(0);
                pieces[3].setX(5);
                lastPieceToMove = pieces[0];
              }
            }
          }
          else if((destX==2) && (destY==0)){
            if(board[0][1]==null && board[0][2]==null && board[0][3]==null)
              if(pieces[2].hasntMoved()){
              if(!castleBlockedUpLeft()){
                pieces[0].setY(0);
                pieces[0].setX(2);
                pieces[2].setY(0);
                pieces[2].setX(3);
                lastPieceToMove = pieces[0];
              }
            }
          }
        }
      }//for blck pieces
    }//if its a instanceof king
  }//end castling method
  
  private boolean castleBlockedUpLeft(){
    boolean castleBlocked = false;
    for(int i=17;i<32;i++){
      if(pieces[i].moveIsValid(1,0,board,pieces))
        castleBlocked = true;
      else if(pieces[i].moveIsValid(2,0,board,pieces))
        castleBlocked = true;
      else if(pieces[i].moveIsValid(3,0,board,pieces))
        castleBlocked = true;
    }
    return castleBlocked;
  }
  private boolean castleBlockedUpRight(){
    boolean castleBlocked = false;
    for(int i=17;i<32;i++){
      if(pieces[i].moveIsValid(5,0,board,pieces))
        castleBlocked = true;
      else if(pieces[i].moveIsValid(6,0,board,pieces))
        castleBlocked = true;
    }
    return castleBlocked;
  }
  private boolean castleBlockedDownLeft(){
    boolean castleBlocked = false;
    for(int i=1;i<16;i++){
      if(pieces[i].moveIsValid(1,7,board,pieces))
        castleBlocked = true;
      else if(pieces[i].moveIsValid(2,7,board,pieces))
        castleBlocked = true;
      else if(pieces[i].moveIsValid(3,7,board,pieces))
        castleBlocked = true;
    }
    return castleBlocked;
  }
  private boolean castleBlockedDownRight(){
    boolean castleBlocked = false;
    for(int i=1;i<16;i++){
      if(pieces[i].moveIsValid(5,7,board,pieces))
        castleBlocked = true;
      else if(pieces[i].moveIsValid(6,7,board,pieces))
        castleBlocked = true;
    }    
    return castleBlocked;
  }
  
  private boolean kingCanMove2(){
    int eight = 0;
    int x = 0;
    if(lastPieceToMove.getColor().equals("Black"))
      x = 16;
    boolean canMove = true;
    boolean zero = true;
    boolean one = true;
    boolean two = true;
    boolean three = true;
    boolean four = true;
    boolean five = true;
    boolean six = true;
    boolean seven = true;
    Piece tempPiece = new Piece(pieces[x].getX(),pieces[x].getY(),pieces[x].getColor());
    int j = lastPieceToMove.getX();
    int k = lastPieceToMove.getY();
    
    
    if(pieces[x].getX()==0 && pieces[x].getY()==0){//top left corner
      eight = eight + 3;
      zero = false;
      two = false;
      four = false;
      five = false;
      six = false;
    }
    else if(pieces[x].getX()==7 && pieces[x].getY()==0){//top right corner
      eight = eight + 3;
      zero = false;
      three = false;
      four = false;
      five = false;
      seven = false;
    }
    else if(pieces[x].getX()==0 && pieces[x].getY()==7){//bottom left corner
      eight = eight + 3;
      one = false;
      two = false;
      four = false;
      six = false;
      seven = false;
    }
    else if(pieces[x].getX()==7 && pieces[x].getY()==7){//bottom right corner
      eight = eight + 3;
      one = false;
      three = false;
      five = false;
      six = false;
      seven = false;
    }
    else if(pieces[x].getX()==0){//touching left wall
      eight = eight + 3;
      two = false;
      four = false;
      six = false;
    }
    else if(pieces[x].getX()==7){//touching right wall
      eight = eight + 3;
      three = false;
      five = false;
      seven = false;
    }
    else if(pieces[x].getY()==0){//touching top wall
      eight = eight + 3;
      zero = false;
      four = false;
      five = false;
    }
    else if(pieces[x].getY()==7){//touching bottom wall
      eight = eight + 3;
      one = false;
      six = false;
      seven = false;
    }
    int y = 0;
    int z = 2;
    while(z>0){
      
      if(zero){//up
        //System.out.println("up");
        if(pieceAtSpot(pieces[x].getX(),pieces[x].getY()+1)==pieces[x].getColor()){
          eight++;
          zero = false;
        }
        else{
          if(z==1)
            y = removePiece();
          pieces[x].movePiece(pieces[x].getX(),pieces[x].getY()-1, board, pieces);
          updateBoard();
          //System.out.println("up");
          if(canPieceMoveToKingsCoords(pieces[x])){
            if(z==1)
              restorePiece(y, j, k);
            // System.out.println("up");
            eight++;
            zero = false;
          }
        }
      }//end zero
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      
      if(one){//down
        // System.out.println("down");
        if(pieceAtSpot(pieces[x].getX(),pieces[x].getY()+1)==pieces[x].getColor()){
          eight++;
          one = false;
        }
        else{
          if(z==1)
            y = removePiece();
          pieces[x].movePiece(pieces[x].getX(),pieces[x].getY()+1, board, pieces);
          updateBoard();
          //  System.out.println("down");
          if(canPieceMoveToKingsCoords(pieces[x])){
            //  System.out.println("down");
            if(z==1)
              restorePiece(y, j, k);
            eight++;
            one = false;
          }
        }
      }//end one
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      
      if(two){//left
        // System.out.println("left");
        if(pieceAtSpot(pieces[x].getX()-1,pieces[x].getY())==pieces[x].getColor()){
          eight++;
          two = false;
        }
        else{
          if(z==1)
            y = removePiece();
          pieces[x].movePiece(pieces[x].getX()-1,pieces[x].getY(), board, pieces);
          updateBoard();
          // System.out.println("left");
          if(canPieceMoveToKingsCoords(pieces[x])){
            if(z==1)
              restorePiece(y, j, k);
            // System.out.println("left");
            eight++;
            two = false;
          }
        }
      }//end two
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      
      
      if(three){//right
        //System.out.println("right");
        if(pieceAtSpot(pieces[x].getX()+1,pieces[x].getY())==pieces[x].getColor()){
          eight++;
          three = false;
        }
        else{
          if(z==1)
            y = removePiece();
          pieces[x].movePiece(pieces[x].getX()+1,pieces[x].getY(), board, pieces);
          updateBoard();
          //System.out.println("right");
          if(canPieceMoveToKingsCoords(pieces[x])){
            if(z==1)
              restorePiece(y, j, k);
            //System.out.println("right");
            eight++;
            three = false;
          }
        }
      }//end three
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      
      if(four){//up left
        //System.out.println("up left");
        if(pieceAtSpot(pieces[x].getX()-1,pieces[x].getY()-1)==pieces[x].getColor()){
          eight++;
          four = false;
        }
        else{
          if(z==1)
            y = removePiece();
          //System.out.println("up left");
          pieces[x].movePiece(pieces[x].getX()-1,pieces[x].getY()-1, board, pieces);
          updateBoard();
          //System.out.println("up left");
          if(canPieceMoveToKingsCoords(pieces[x])){
            if(z==1)
              restorePiece(y, j, k);
            eight++;            
            four = false;
          }
        }
      }//end four
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      
      if(five){//up right
        //System.out.println("up right");
        if(pieceAtSpot(pieces[x].getX()+1,pieces[x].getY()-1)==pieces[x].getColor()){
          eight++;
          five = false;
        }
        else{
          if(z==1)
            y = removePiece();
          //System.out.println("up right");
          pieces[x].movePiece(pieces[x].getX()+1,pieces[x].getY()-1, board, pieces);
          updateBoard();
          //System.out.println("up right");
          if(canPieceMoveToKingsCoords(pieces[x])){
            if(z==1)
              restorePiece(y, j, k);
            eight++; 
            five = false;
          }
        }
      }//end five
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      
      if(six){//down left
        //System.out.println("down left");
        if(pieceAtSpot(pieces[x].getX()-1,pieces[x].getY()+1)==pieces[x].getColor()){
          eight++;
          six = false;
        }
        else{
          if(z==1)
            y = removePiece();
          //System.out.println("down left");
          pieces[x].movePiece(pieces[x].getX()-1,pieces[x].getY()+1, board, pieces);
          updateBoard();
          //System.out.println("down left");
          if(canPieceMoveToKingsCoords(pieces[x])){
            if(z==1)
              restorePiece(y, j, k);
            eight++; 
            six = false;
          }
        }
      }//end six 
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      
      if(seven){//down right
        //System.out.println("down right");
        if(pieceAtSpot(pieces[x].getX()+1,pieces[x].getY()+1)==pieces[x].getColor()){
          eight++;
          seven = false;
        }
        else{
          if(z==1)
            y = removePiece();
          //System.out.println("down right");
          pieces[x].movePiece(pieces[x].getX()+1,pieces[x].getY()+1, board, pieces);
          updateBoard();
          if(canPieceMoveToKingsCoords(pieces[x])){
            if(z==1)
              restorePiece(y, j, k);
            //System.out.println("down right");
            eight++;
            seven = false;
          }
        }
      }//end seven
      
      pieces[x].setX(tempPiece.getX());
      pieces[x].setY(tempPiece.getY());
      updateBoard();
      z--;
    }
    
    if(canPieceCaptureCheck())
      eight--;
    
    if(eight == 8)
      canMove = false;
    //System.out.println(eight);
    return canMove;    
  }
  
  private boolean canPieceCaptureCheck(){
    boolean canCapture = false;
    if(lastPieceToMove.getColor().equals("Black")){
      for(int i=17;i<31;i++)
        if(pieces[i].moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(),board,pieces))
        canCapture = true;
    }
    else if (lastPieceToMove.getColor().equals("White")){
      for(int i=1;i<15;i++)
        if(pieces[i].moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(),board,pieces))
        canCapture = true;
    }
    return canCapture;
  }
  
  private int removePiece(){
    int a = 0;
    //System.out.println("remove" + lastPieceToMove.getX() + "x" + lastPieceToMove.getY() + "y");
    for(int i=0;i<pieces.length;i++)
      if(pieces[i].getX()==lastPieceToMove.getX() && pieces[i].getY()==lastPieceToMove.getY()){
      pieces[i].setX(-1);
      pieces[i].setY(-1);
      a=i;
    }
    return a;
  }
  
  private void restorePiece(int i, int j, int k){
    pieces[i].setX(j);
    pieces[i].setY(k);
    //System.out.println("restore" + j + "x" + k + "y");
    //System.out.println(pieces[i].getX() + "x" + pieces[i].getY() + "y");
  }
  
  private boolean canAPieceMove(){
    boolean aPieceCanMove = false;
    if(lastPieceToMove.getColor().equals("Black")){
      for(int a=16;a<31;a++){
        for(int b=0;b<board.length;b++){
          for(int c=0;c<board[b].length;c++){
            if(pieces[a].moveIsValid(b,c,board,pieces))
              aPieceCanMove = true;
          }
        }
      }
    }
    else if(lastPieceToMove.getColor().equals("White")){
      for(int a=0;a<15;a++){
        for(int b=0;b<board.length;b++){
          for(int c=0;c<board[b].length;c++){
            if(pieces[a].moveIsValid(b,c,board,pieces))
              aPieceCanMove = true;
          }
        }
      }
    }
    return aPieceCanMove;
  }
  
  public boolean getStalemate(){
    if(!canAPieceMove())
      stalemate = true;
    return stalemate;
  }
}//end class