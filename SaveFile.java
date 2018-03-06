import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SaveFile{
	
	Path[] saves;
	ObjectInputStream input;
	ObjectOutputStream output;
	Scanner sc;
	Board board;

	public SaveFile(Board board){
		this.board = board;
		sc = new Scanner(System.in);
		saves = new Path[10];
		addSavesToArray();
	}

	public void askToLoad(){
		System.out.println("Type 'load' to load a saved game or 'new' to play a new game");
		String choice = sc.next();
		if(choice.equals("load")){
			displaySaves();
			try{
			board = loadSave(sc.nextInt());
			}
			catch(Exception e){
				System.err.println("Nigga that aint an integer");
			}
		}
	}

	public Board getBoard(){
		return this.board;
	}

	/*fix this to make it just check for the last ".ser" to indentify files
	public void addSavesToArray(){
		for(int a=0;a<saves.length;a++)
			if(Files.exists(Paths.get("chessSave"+a+".ser")))
				saves[a] = Paths.get("chessSave"+a+".ser");
	}*/
	//search directory for strings that have .ser as last three chars
	//add those to Path array
	public void addSavesToArray(){
		int indexOfPathArray = 0;
		String[] foldersFiles = null;
		File saveDirectory = new File(".");
		foldersFiles = saveDirectory.list();

		for(int a=0;a<foldersFiles.length;a++)
			if(foldersFiles[a].endsWith(".ser")){
				saves[indexOfPathArray] = Paths.get(foldersFiles[a]);
				indexOfPathArray++;
			}



	}

	public void displaySaves(){
		addSavesToArray();
		for(int a=0;a<saves.length;a++)
			if(saves[a]==null)
				System.out.println(a+") empty");
			else
				System.out.println(a+") "+saves[a].toString());

	}

	public Board loadSave(int s){
		Board gameBoard = null;
		try{
		//Path path = saves[s];
		System.out.println("1");
		input = new ObjectInputStream(new FileInputStream(saves[s].toString()));
		System.out.println(saves[s].toString());
		System.out.println("2");
		//gameBoard = (Board) input.readObject();
		Object board123 = input.readObject();
		gameBoard = (Board) board123;
		System.out.println("3");
		System.out.println(gameBoard.lastPieceToMove.getColor());
		input.close();
		}
		catch(FileNotFoundException e){
			System.err.println("Sorry that file does not exist");
		}
		catch(IOException e){
			System.err.println("you fucked up");
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			System.err.println("what kind of exception is this anyway");
		}

		return gameBoard;
	}

	public void saveGame(String s, Board gameBoard){
		try{
		output = new ObjectOutputStream(new FileOutputStream(s+".ser"));
		output.writeObject(gameBoard);
		output.close();
		//System.out.println(gameBoard.lastPieceToMove.getColor());
		}
		catch(FileNotFoundException e){
			System.err.println("Sorry that file does not exist");
		}
		catch(IOException e){
			//System.err.println("you fucked up");
			//e.printStackTrace();
		}
	}
}