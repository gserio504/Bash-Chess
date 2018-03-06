import java.io.Serializable;

public class Piece implements Serializable{
  int xCoord;
  int yCoord;
  String color;
  boolean hasntMoved = true;
  static boolean blackKingInCheck = false;
  static boolean whiteKingInCheck = false;
  static Piece lastPieceToMove;
  
  
  public Piece(int x, int y,String color){
    xCoord = x;
    yCoord = y;
    this.color = color;
  }
  
  public Piece(int x, int y){
  }
  
  public void setBlackKingInCheck(){
    blackKingInCheck = true;
  }
  
  public void setWhiteKingInCheck(){
    whiteKingInCheck = true;
  }
  
  public boolean getBlackKingInCheck(){
    return blackKingInCheck;
  }
  
  public boolean getWhiteKingInCheck(){
    return whiteKingInCheck;
  }
  
  public void setLastPieceToMove(Piece piece){
    lastPieceToMove = piece;
  }
  
  public void setColor(String color){
    this.color = color;
  }
  
  public String getColor(){
    return color;
  }
  
  public int getX(){
    return xCoord;
  }
  
  public int getY(){
    return yCoord;
  }
  
  public void setX(int x){
    xCoord = x;
  }
  
  public void setY(int y){
    yCoord = y;
  }
  
  public boolean hasntMoved(){
    return hasntMoved;
  }
  
  public void setHasntMoved(){
    hasntMoved = false;
  }
  
  public void setNull(){
    //xCoord = null;
    //yCoord = null;
    color = null;
  }
  
//returns true if the piece was moved, false if it wasn't
  public boolean movePiece(int destX, int destY, Piece[][] board,Piece[] pieces){
    boolean pieceIsMoved = false;
    //is the move valid?
    if(moveIsValid(destX, destY, board, pieces)){
      /*if it is, remove rook from last position
       and update the rook's position to where intended*/
      pieceIsMoved = true;
      setX(destX);
      setY(destY);
      
    }
    return pieceIsMoved;
  }
  
  public boolean moveIsValid(int destX, int destY, Piece[][] board,Piece[] pieces){
    boolean moveIsValid = false;
    
    if(getY()>=0 && getY()<8 && getX()>=0 && getX()<8){//this line is so that pieces who are captured
      if(board[getY()][getX()] instanceof King){ //and thus have a x and y value of -1 dont throw an exception
        if(moveIsValidKing(destX, destY, board, pieces))
          moveIsValid = true;
      }
      else if(board[getY()][getX()] instanceof Queen){
        if(moveIsValidQueen(destX, destY, board, pieces))
          moveIsValid = true;
      }
      else if(board[getY()][getX()] instanceof Rook){
        if(moveIsValidRook(destX, destY, board, pieces))
          moveIsValid = true;
      }
      else if(board[getY()][getX()] instanceof Bishop){
        if(moveIsValidBishop(destX, destY, board, pieces))
          moveIsValid = true;
      }
      else if(board[getY()][getX()] instanceof Knight){
        if(moveIsValidKnight(destX, destY, board, pieces))
          moveIsValid = true;
      }
      else if(board[getY()][getX()] instanceof Pawn){
        if(moveIsValidPawn(destX, destY, board, pieces))
          moveIsValid = true;
      }
    }
    return moveIsValid;
  }
  
  public boolean moveIsValidKing(int destX, int destY, Piece[][]board,Piece[] pieces){
    boolean validMove = false;
    
    //check if move is a spot on the board
    if((destX<8 && destX>=0) &&(destY<8 && destY>=0)){
      
      //for moving vertically
      if(destX==getX()){
        if((destY==(getY()+1)) || (destY==(getY()-1)))
          validMove = true;
      }
      //for moving horizontally
      else if(destY==getY()){
        if((destX==(getX()+1)) || (destX==(getX()-1)))
          validMove = true;
      }  //diagonally down right
      
      if((destX==getX()+1) && (destY==getY()+1))
        validMove = true;//down left
      else if((destX==getX()-1) && (destY==getY()+1))
        validMove = true;//diagonally up right
      else if((destX==getX()+1) && (destY==getY()-1))
        validMove = true; //up left
      else if((destX==getX()-1) && (destY==getY()-1))
        validMove = true;
    }
    
    if(movementBlockedKing(validMove, destX, destY, board, pieces))
      validMove = false;
    
    
    if(validMove == true){
      hasntMoved = false;
      if(getColor().equals("White"))
        whiteKingInCheck = false;
      else if(getColor().equals("Black"))
        blackKingInCheck = false;
    }
    
    return validMove; 
  }
  
  private boolean movementBlockedKing(boolean validMove, int destX, int destY, Piece[][] board, Piece[] pieces){
    //THIS IS A WAY BETTER IMPLEMENTATION THAN ALL THE OTHER PIECES, MIGHT WANT TO CHANGE THEM LATER
    boolean movementBlocked = false; //what this method returns
    if(validMove == true){
      if((board[destY][destX]!= null) && (board[destY][destX].getColor() == getColor()))
        movementBlocked = true;
    }
    //check if the spot the king is moivng to would put him in check
    //see if any piece has a valid move there
    if(getColor().equals("White")){
      for(int i=1; i<15; i++)
        if(pieces[i].moveIsValid(destX, destY, board, pieces))
        movementBlocked = true;
      
    }
    else if(getColor().equals("Black")){
      for(int j=17; j<32; j++)
        if(pieces[j].moveIsValid(destX, destY, board, pieces)){
        movementBlocked = true;
        //System.out.println(j + " " + destX + destY);
      }
    }
    
    //make it so king cant move to where a pawn could capture him
    //diagonally left
    if(getColor().equals("White")){
      if(destX-1 >=0 && (destY-1)>=0){
        if(board[destY-1][destX-1]!=null){
          if(board[destY-1][destX-1] instanceof Pawn && board[destY-1][destX-1].getColor()!=getColor())
            movementBlocked = true;
        }
      }
      //diagonally right
      if(destX >=0 && (destX+1)<8 && (destY-1)>=0 && destY<8){
        if(board[destY-1][destX+1]!=null){
          if(board[destY-1][destX+1] instanceof Pawn&& board[destY-1][destX+1].getColor()!=getColor())
            movementBlocked = true;
        }
      }
    }
    else{
      if((destX-1) >=0 && destX<8 && destY>=0 && (destY+1)<8){
        //diagonally left
        if(board[destY+1][destX-1]!=null){
          if(board[destY+1][destX-1] instanceof Pawn&& board[destY+1][destX-1].getColor()!=getColor())
            movementBlocked = true;
        }
      }
      if(destX >=0 && (destX+1)<8 && destY>=0 && (destY+1)<8){
        //diagonally right
        if(board[destY+1][destX+1]!=null){
          if(board[destY+1][destX+1] instanceof Pawn&& board[destY+1][destX+1].getColor()!=getColor())
            movementBlocked = true;
        }
      }
    }
    
    //block movement where a king would put another king in check
    if(destY!=0){
      if(board[destY-1][destX] instanceof King) //up
        if(board[destY-1][destX].getColor()!=getColor())
        movementBlocked = true;
    }
    if(destY!=7){
      if(board[destY+1][destX] instanceof King) //down
        if(board[destY+1][destX].getColor()!=getColor())
        movementBlocked = true;   
    }
    if(destX!=0){
      if(board[destY][destX-1] instanceof King) //left
        if(board[destY][destX-1].getColor()!=getColor())
        movementBlocked = true;   
    }
    if(destX!=7){
      if(board[destY][destX+1] instanceof King)//right
        if(board[destY][destX+1].getColor()!=getColor())
        movementBlocked = true;     
    }
    if(destX!=0 && destY!=0){
      if(board[destY-1][destX-1] instanceof King)//up left
        if(board[destY-1][destX-1].getColor()!=getColor())
        movementBlocked = true;
    }
    if(destX!=7 && destY!=0){
      if(board[destY-1][destX+1] instanceof King) //up right
        if(board[destY-1][destX+1].getColor()!=getColor())
        movementBlocked = true;    
    }
    if(destX!=0 && destY!=7){
      if(board[destY+1][destX-1] instanceof King) //down left
        if(board[destY+1][destX-1].getColor()!=getColor())
        movementBlocked = true;     
    }
    if(destX!=7 && destY!=7){
      if(board[destY+1][destX+1] instanceof King) //down right
        if(board[destY+1][destX+1].getColor()!=getColor())
        movementBlocked = true;
    }
    
    return movementBlocked;
  }
//MOVE IS VALID QUEEN
  public boolean moveIsValidQueen(int x, int y, Piece[][]board, Piece[] pieces){  
    boolean validMove = false;
    double destX = x;
    double destY = y;
    
    
    if((destX<8 && destX>=0) &&(destY<8 && destY>=0)){
      //horizontal and vertical movement
      if((destX==getX()) || (destY==getY()))
        validMove = true;
      //check if slope of line is 1 or -1 for diagonal movement
      if((destX-getX())!=0){ //<-- prevents divide by zero
        if((((destY-getY())/(destX-getX()))==1) || (((destY-getY())/(destX-getX()))==(-1)))
          validMove = true;
      }
    }
    
    if(movementBlockedQueen(destX, destY, board, pieces))
      validMove = false;
    
    return validMove; 
  }
  
  public boolean movementBlockedQueen(double destX, double destY, Piece[][] board, Piece[] pieces){  
    boolean movementBlocked = false; //what this method returns
    int j = 0;  
    //copypasta'd from rook for horizontal and vertical movement
    //movement right  
    int deltaX = 0;
    int deltaY = 0;
    
    if(destX==getX())
      deltaY = ((int)destY - getY());//find change in y  
    else if(destY==getY())
      deltaX = ((int)destX - getX());//find change in x
    
    //once direction of change has been found determine which direction, utilizes
    //booleans xChanged and yChanged
    if(deltaX > 0){
      for(int i=(getX()+1); i<destX; i++)
        if((board[getY()][i]!=null))
        movementBlocked = true;
    }
    
    //movement left
    else if(deltaX < 0){
      for(int i=(getX()-1); i>(getX() + deltaX); i--)
        if(board[getY()][i]!=null)
        movementBlocked = true;
    }
    //movement up
    else if(deltaY < 0){
      for(int i=(getY()-1); i>(getY() + deltaY); i--)
        if(board[i][getX()]!=null)                            
        movementBlocked = true;
    }
    //movement down
    else if(deltaY > 0){
      for(int i=(getY()+1); i<destY; i++)
        if((board[i][getX()]!=null))
        movementBlocked = true;
    }
    
    
    //copy pasta'd from bishop for diagonally movement
    //diagonally down right
    if((destX>getX())&&(destY>getY())){ //below checks if first spot empty and is ally/enemy
      if(board[getY()+1][getX()+1]!=null && board[getY()+1][getX()+1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      
      for(int i=(getX()+1); i<destX; i++){ //checks for the rest after that
        j++;
        if((getY()+j) <8 && i <8){
          if(board[getY()+j][i]!=null)
            movementBlocked = true;
        }
      }
    }
    //}
    //}
    j = 0;
    
    //diagonally up left
    if((destX<getX())&&(destY<getY())){
      if(board[getY()-1][getX()-1]!=null && board[getY()-1][getX()-1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      for(int i=(getX()-1); i>destX; i--){
        j++;
        if((getY()-j) >=0 && i >=0)
          if(board[getY()-j][i]!=null)
          movementBlocked = true;
      }
    }
    j=0;
    //diagonally down left
    if((destX<getX())&&(destY>getY())){
      if(board[getY()+1][getX()-1]!=null && board[getY()+1][getX()-1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      
      for(int i=(getX()-1); i>destX; i--){
        j++;
        if((getY()+j) < 8 && i >= 0){
          if(board[getY()+j][i]!=null)
            movementBlocked = true;
        }
      }
    }
    j=0;
    
    //diagonally up right
    if((destX>getX())&&(destY<getY())){
      if(board[getY()-1][getX()+1]!=null && board[getY()-1][getX()+1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      for(int i=(getX()+1); i<destX; i++){
        j++;
        if((getY()-j) >=0 && i <8){ 
          if(board[getY()-j][i]!=null)
            movementBlocked = true;
        }
      }
    }  
    //so queen cant move to its teammates spot
    if(board[(int)destY][(int)destX]!=null && board[(int)destY][(int)destX].getColor()==this.color)
      movementBlocked = true;
    
    
    
    if(whiteKingInCheck && getColor().equals("White"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    else if(blackKingInCheck && getColor().equals("Black"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    
    return movementBlocked;
  }
//MOVEMENT VALID BISHOP
  public boolean moveIsValidBishop(int x, int y, Piece[][] board, Piece[] pieces){
    boolean validMove = false;
    double destX = x;
    double destY = y;
    //check if slope of line is 1 or -1
    
    if((destX-getX())!=0){ // <<<<< prevents divide by zero bullshit
      if((((destY-getY())/(destX-getX()))==1) || (((destY-getY())/(destX-getX()))==(-1))){
        if((destX<8 && destX>=0) &&(destY<8 && destY>=0))
          validMove = true;
      }
    }
    
    if(movementBlockedBishop(destX, destY, board, pieces))
      validMove = false;
    
    return validMove; 
  }
//MOVEMENT BLOCKED BISHOP
  private boolean movementBlockedBishop(double destX, double destY, Piece[][] board, Piece[] pieces){  
    boolean movementBlocked = false; //what this method returns
    int j = 0;
    
    //diagonally down right
    if((destX>getX())&&(destY>getY())){ //below checks if first spot empty and is ally/enemy
      if(board[getY()+1][getX()+1]!=null && board[getY()+1][getX()+1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      
      for(int i=(getX()+1); i<destX; i++){ //checks for the rest after that
        j++;
        if((getY()+j)<8 && i<8)
          if(board[getY()+j][i]!=null)
          movementBlocked = true;
      }
    }
    j = 0;
    
    //diagonally up left
    if((destX<getX())&&(destY<getY())){
      if(board[getY()-1][getX()-1]!=null && board[getY()-1][getX()-1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      for(int i=(getX()-1); i>destX; i--){
        j++;
        if(i>=0 && (getY()-j)>=0)
          if(board[getY()-j][i]!=null)
          movementBlocked = true;
      }
    }
    j=0;
    //diagonally down left
    if((destX<getX())&&(destY>getY())){
      if(board[getY()+1][getX()-1]!=null && board[getY()+1][getX()-1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      
      for(int i=(getX()-1); i>destX; i--){
        j++;
        if(i>=0 && (getY()+j)<8)
          if(board[getY()+j][i]!=null)
          movementBlocked = true;
      }
    }
    j=0;
    
    //diagonally up right
    if((destX>getX())&&(destY<getY())){
      if(board[getY()-1][getX()+1]!=null && board[getY()-1][getX()+1].getColor()==this.getColor()) //checks if first spot is empty
        movementBlocked = true;
      for(int i=(getX()+1); i<destX; i++){
        j++;
        if((getY()-j)>=0 && i<8)
          if(board[getY()-j][i]!=null)
          movementBlocked = true;
      }
    }  
    
    if(whiteKingInCheck && getColor().equals("White"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    else if(blackKingInCheck && getColor().equals("Black"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    
    return movementBlocked;
  }
  
//MOVE IS VALID ROOK
  private boolean moveIsValidRook(int destX, int destY, Piece[][] board, Piece[] pieces){
    boolean validMove = false;
    
    //checks if rooks can move in that way
    if((destX==getX()) || (destY==getY()))
      if((destX<8 && destX>=0) &&(destY<8 && destY>=0)){
      validMove = true;
      
    }
    
    if(movementBlockedRook(destX, destY, board, pieces))
      validMove = false;
    
    if(validMove == true)
      hasntMoved = false;
    
    return validMove; 
  }
  
//MOVEMENT BLOCKED ROOK
  private boolean movementBlockedRook(int destX, int destY, Piece[][] board, Piece[] pieces){
    
    boolean movementBlocked = false; //what this method returns
    int deltaX = 0;
    int deltaY = 0;
    
    
    if(destX==getX())
      deltaY = (destY - getY());//find change in y  
    else if(destY==getY())
      deltaX = (destX - getX());//find change in x
    
    //once direction of change has been found determine which direction, utilizes
    //booleans xChanged and yChanged
    
    //movement right
    if(deltaX > 0){
      for(int i=(getX()+1); i<destX; i++)
        if((board[getY()][i]!=null))
        movementBlocked = true;
    }
    
    //movement left
    else if(deltaX < 0){
      for(int i=(getX()-1); i>(getX() + deltaX); i--)
        if(board[getY()][i]!=null)
        movementBlocked = true;
    }
    //movement up
    else if(deltaY < 0){
      for(int i=(getY()-1); i>(getY() + deltaY); i--)
        if(board[i][getX()]!=null)                            
        movementBlocked = true;
    }
    //movement down
    else if(deltaY > 0){
      for(int i=(getY()+1); i<destY; i++)
        if((board[i][getX()]!=null))
        movementBlocked = true;
    }
    
    if(board[destY][destX]!=null && board[destY][destX].getColor()==this.color)
      movementBlocked = true;
    
    if(whiteKingInCheck && getColor().equals("White"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    else if(blackKingInCheck && getColor().equals("Black"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    
    
    return movementBlocked;
  }
  
//MOVE IS VALID KNIGHT
  private boolean moveIsValidKnight(int destX, int destY, Piece[][] board, Piece[] pieces){
    boolean validMove = false;
    
    //these are reversed on accident
    if((destX<8 && destX>=0) &&(destY<8 && destY>=0)){
      //knight move up and to the right
      if((destX==(getX()+1) && (destY==(getY()+2))))
        validMove = true;
      //knight move up and to the left
      else if((destX==(getX()-1) && (destY==(getY()+2))))
        validMove = true;
      //knight move down and to the right
      else if((destX==(getX()+1) && (destY==(getY()-2))))
        validMove = true;
      //knight move down and to the left
      else if((destX==(getX()-1) && (destY==(getY()-2))))
        validMove = true;
      //move two to left and up one
      else if(((destX==(getX()-2)) && (destY==(getY()+1))))
        validMove = true;
      else if((destX==(getX()-2) && (destY==(getY()-1))))
        validMove = true;
      else if((destX==(getX()+2) && (destY==(getY()+1))))
        validMove = true;
      else if((destX==(getX()+2) && (destY==(getY()-1))))
        validMove = true;
    }
    if(movementBlockedKnight(destX, destY, board, pieces))
      validMove = false;
    
    return validMove; 
  }
  
//MOVEMENT BLOCKED KNIGHT
  private boolean movementBlockedKnight(int destX, int destY, Piece[][] board, Piece[] pieces){
    boolean movementBlocked = false;
    if((destX==(getX()+1) && (destY==(getY()+2))) &&  (board[getY()+2][getX()+1]!=null && board[getY()+2][getX()+1].getColor()==this.getColor()))
      movementBlocked = true;
    //knight move up and to the left
    else if((destX==(getX()-1) && (destY==(getY()+2))) && (board[getY()+2][getX()-1]!=null && board[getY()+2][getX()-1].getColor()==this.getColor()))
      movementBlocked = true;
    //knight move down and to the right
    else if((destX==(getX()+1) && (destY==(getY()-2))) && (board[getY()-2][getX()+1]!=null && board[getY()-2][getX()+1].getColor()==this.getColor()))
      movementBlocked = true;
    //knight move down and to the left
    else if((destX==(getX()-1) && (destY==(getY()-2))) && (board[getY()-2][getX()-1]!=null && board[getY()-2][getX()-1].getColor()==this.getColor()))       
      movementBlocked = true;
    //move two to left and up one
    else if(((destX==(getX()-2)) && (destY==(getY()+1))) && (board[getY()+1][getX()-2]!=null && board[getY()+1][getX()-2].getColor()==this.getColor()))
      movementBlocked = true;
    else if((destX==(getX()-2) && (destY==(getY()-1))) && (board[getY()-1][getX()-2]!=null && board[getY()-1][getX()-2].getColor()==this.getColor()))
      movementBlocked = true;
    else if((destX==(getX()+2) && (destY==(getY()+1))) && (board[getY()+1][getX()+2]!=null && board[getY()+1][getX()+2].getColor()==this.getColor()))
      movementBlocked = true;
    else if((destX==(getX()+2) && (destY==(getY()-1))) && (board[getY()-1][getX()+2]!=null && board[getY()-1][getX()+2].getColor()==this.getColor()))
      movementBlocked = true;
    
    if(whiteKingInCheck && getColor().equals("White"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    else if(blackKingInCheck && getColor().equals("Black"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    
    return movementBlocked;
  }
  
//MOVE IS VALID PAWN
  private boolean moveIsValidPawn(int destX, int destY, Piece[][] board, Piece[] pieces){
    boolean validMove = false;
    
    //make sure destination is a spot on the board
    if((destX<8 && destX>=0) &&(destY<8 && destY>=0)){
      //checks to see if pawn is black or white
      if(getColor().equals("White")){
        //if the pawn hasn't moved, it can move two spots
        if(((getY()==6) && (destY==(getY()-2))) && (getX() == destX))
          validMove = true;
        //for moving the pawn one space up
        else if((destY==(getY()-1)) && (getX() == destX))
          validMove = true;
        
        //FOR CAPTURING UP LEFT
        if((destX==getX()-1) && (destY==getY()-1)){
          if((board[destY][destX]!=null) && (board[destY][destX].getColor()!=getColor()))
            validMove = true;
        }
        //FOR CAPTURING UP RIGHT
        else if((destX==getX()+1) && (destY==getY()-1)){
          if((board[destY][destX]!=null) && (board[destY][destX].getColor()!=getColor()))
            validMove = true;
        }
      }     
      else{
        //black pawn movement
        if(((getY()==1) && (destY==(getY()+2))) && (getX() == destX))
          validMove = true;
        else if((destY == (getY()+1)) && (getX() == destX))
          validMove = true;
        
        //FOR CAPTURING down LEFT
        if((destX==getX()-1) && (destY==getY()+1)){
          if((board[destY][destX]!=null) && (board[destY][destX].getColor()!=getColor()))
            validMove = true;
        }
        //FOR CAPTURING down RIGHT
        else if((destX==getX()+1) && (destY==getY()+1)){
          if((board[destY][destX]!=null) && (board[destY][destX].getColor()!=getColor()))
            validMove = true;
        }
        
      }
    }
    
    if(movementBlockedPawn(destX, destY, board, pieces))
      validMove = false;
    
    return validMove; 
  }
  
//MOVEMENT BLOCKED PAWN
//check if a piece is in the way of movement
  private boolean movementBlockedPawn(int destX, int destY, Piece[][] board, Piece[] pieces){
    
    boolean movementBlocked = false; //what this method returns
    
    if(destY==getY()+1 && destX==getX())
      if((board[getY()+1][getX()]!=null))                           
      movementBlocked = true;
    
    if(destY==getY()-1 && destX==getX())
      if((board[getY()-1][getX()]!=null))                           
      movementBlocked = true;
    
    //movement up two spaces
    if(destY==(getY()-2)){
      if(board[getY()-1][getX()]!=null || board[destY][destX]!=null)                           
        movementBlocked = true;
    }
    
    //movement down two spaces
    if(destY==(getY()+2))
      if((board[getY()+1][getX()]!=null) || board[destY][destX]!=null)
      movementBlocked = true;
    
    if(whiteKingInCheck && getColor().equals("White"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    else if(blackKingInCheck && getColor().equals("Black"))
      if(!moveIsValid(lastPieceToMove.getX(),lastPieceToMove.getY(), board, pieces))
      movementBlocked = true;
    
    return movementBlocked;
  }
  
  
  
  
  public String toString(){
    String what = new String();
    what = "x: " + getX() + " y: " + getY() + " " + getColor();
    return what;
  }
  
}